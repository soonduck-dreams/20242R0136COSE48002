/**
 * ProgressionService
 *
 * [1. 역할]
 * - 화성학에서 코드 진행(Progression)은 여러 코드(Chord)가 연속적으로 배열되어 음악의 조화로운 흐름을 구성하는 단위.
 * - 이 클래스는 코드 진행 데이터를 생성, 수정, 삭제, 조회하는 기능을 제공.
 *
 * [2. 주요 기능]
 * - 새로운 코드 진행을 생성하고 관련 파일(오디오, MIDI 샘플)을 저장.
 * - 기존 코드 진행의 정보를 업데이트하고 포함된 코드(Chord) 목록 수정.
 * - 코드 진행에 연관된 파일 삭제 또는 수정 처리.
 * - 그 코드 진행에 포함된 코드(Chord) 목록 조회.
 * - 특정 코드가 포함된 코드 진행 목록 조회.
 * - 음악을 끝맺는 데 적합한 코드 진행(cadence progression) 중 하나를 랜덤으로 선택해 조회.
 *
 * [3. 사용 사례]
 * - 관리자 페이지에서 새로운 코드 진행을 생성하거나 기존 코드 진행 정보를 수정/삭제.
 * - 특정 코드가 포함된 코드 진행 목록을 조회하거나 특정 코드 진행의 세부 정보를 확인.
 * - 음악을 끝맺는 데 적합한 코드 진행(cadence progression) 중 하나를 랜덤으로 선택해 해당 MIDI 샘플을 음악 생성 요청에 사용.
 *
 */

package jhhan.harmonynow_backend.service;

import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.domain.ChordProgressionMap;
import jhhan.harmonynow_backend.domain.Progression;
import jhhan.harmonynow_backend.dto.ChordNameIdDTO;
import jhhan.harmonynow_backend.dto.CreateProgressionDTO;
import jhhan.harmonynow_backend.dto.EditProgressionDTO;
import jhhan.harmonynow_backend.dto.ReadProgressionDTO;
import jhhan.harmonynow_backend.repository.ChordProgressionMapRepository;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.repository.ProgressionRepository;
import jhhan.harmonynow_backend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgressionService {

    private final ProgressionRepository progressionRepository;
    private final ChordRepository chordRepository;
    private final ChordProgressionMapRepository mapRepository;
    private final FileUtils fileUtils;

    @Transactional
    public void saveProgression(CreateProgressionDTO dto) {
        Progression progression = Progression.CreateProgression(dto);
        Long savedId = progressionRepository.save(progression);

        try {
            String audioUrl = FileUtils.saveAudioIfExists(dto.getAudioFile(), "progression/audio", savedId);
            progression.setAudioUrl(audioUrl);
        } catch (Exception e) {
            throw new RuntimeException("롤백", e);
        }

        try {
            String midiUrl = FileUtils.saveMidiIfExists(dto.getSampleMidiFile(), "progression/sample_midi", savedId);
            progression.setSampleMidiUrl(midiUrl);
        } catch (Exception e) {
            throw new RuntimeException("롤백", e);
        }

        for (int i = 0; i < dto.getChordIds().size(); i++) {
            Long chordId = dto.getChordIds().get(i);
            if (chordId == null || chordId < 0) {
                break;
            }

            Chord chord = chordRepository.findOne(chordId);
            ChordProgressionMap map = ChordProgressionMap.createChordProgressionMap(chord, progression, i);
            mapRepository.save(map);
        }
    }

    public String getProgressionName(Progression progression) {
        List<ChordNameIdDTO> dtoList = chordRepository.findChordNameIdByProgressionId(progression.getId());

        String name = "";
        for (int i = 0; i < dtoList.size(); i++) {
            name += dtoList.get(i).getName();
            if (i != dtoList.size() - 1) {
                name += " - ";
            }
        }

        return name;
    }

    public String getProgressionName(Progression progression, Long chordId) {
        List<ChordNameIdDTO> dtoList = chordRepository.findChordNameIdByProgressionId(progression.getId());

        String name = "";
        for (int i = 0; i < dtoList.size(); i++) {

            if (dtoList.get(i).getId().equals(chordId)) {
                name += "<strong>" + dtoList.get(i).getName() + "</strong>";
            } else {
                name += dtoList.get(i).getName();
            }

            if (i != dtoList.size() - 1) {
                name += " - ";
            }
        }

        return name;
    }

    public List<Long> getChordIdsInProgression(Progression progression) {
        List<ChordNameIdDTO> dtoList = chordRepository.findChordNameIdByProgressionId(progression.getId());

        List<Long> ids = new ArrayList<>();
        for (ChordNameIdDTO dto : dtoList) {
            ids.add(dto.getId());
        }

        return ids;
    }

    public List<ReadProgressionDTO> getProgressionsByChordId(Long chordId) {
        Chord chord = chordRepository.findOne(chordId);
        List<Progression> progressions = mapRepository.findProgressionsByChord(chord);

        List<ReadProgressionDTO> dtoList = new ArrayList<>();
        for (Progression p : progressions) {
            dtoList.add(new ReadProgressionDTO(p.getId(), getProgressionName(p, chordId), p.getDescription(), p.getAudioUrl(), p.getSampleMidiUrl(), p.getIsCadence()));
        }

        return dtoList;
    }

    public ReadProgressionDTO getProgressionById(Long progressionId, boolean isSampleMidiUrlNeeded) {
        Progression progression = progressionRepository.findOne(progressionId);
        return new ReadProgressionDTO(progression.getId(), getProgressionName(progression), progression.getDescription(), progression.getAudioUrl(),
                isSampleMidiUrlNeeded ? progression.getSampleMidiUrl() : null, progression.getIsCadence());
    }

    @Transactional
    public void updateProgression(Long progressionId, EditProgressionDTO dto) {
        Progression progression = progressionRepository.findOne(progressionId);
        progression.updateProgression(dto);

        /* Progression에 포함된 Chord 목록 수정 */
        List<ChordProgressionMap> maps = progression.getMaps();

        List<Long> updatedChordIds = new ArrayList<>();
        for (Long chordId : dto.getChordIds()) {
            if (chordId == null || chordId < 0) {
                break;
            }
            updatedChordIds.add(chordId);
        }

        int min = Math.min(maps.size(), updatedChordIds.size());

        for (int i = 0; i < min; i++) {
            ChordProgressionMap map = maps.get(i);
            map.updateChordProgressionMap(i, chordRepository.findOne(updatedChordIds.get(i)));
        }

        for (int i = min; i < updatedChordIds.size(); i++) {
            ChordProgressionMap map = ChordProgressionMap.createChordProgressionMap(chordRepository.findOne(updatedChordIds.get(i)), progression, i);
            mapRepository.save(map);
        }

        for (int i = min; i < maps.size(); i++) {
            mapRepository.delete(maps.get(i).getId());
        }

        /* 파일 수정 */
        if (Boolean.TRUE.equals(dto.getIsAudioDeleteRequested()) && FileUtils.deleteFile(progression.getAudioUrl())) {
            progression.setAudioUrl(null);
        } else {
            try {
                String audioUrl = FileUtils.saveAudioIfExists(dto.getAudioFile(), "progression/audio", progressionId);
                if (audioUrl != null) {
                    progression.setAudioUrl(audioUrl);
                }
            } catch (Exception e) {
                throw new RuntimeException("롤백", e);
            }
        }

        if (Boolean.TRUE.equals(dto.getIsSampleMidiDeleteRequested()) && FileUtils.deleteFile(progression.getSampleMidiUrl())) {
            progression.setSampleMidiUrl(null);
        } else {
            try {
                String sampleMidiUrl = FileUtils.saveMidiIfExists(dto.getSampleMidiFile(), "progression/sample_midi", progressionId);
                if (sampleMidiUrl != null) {
                    progression.setSampleMidiUrl(sampleMidiUrl);
                }
            } catch (Exception e) {
                throw new RuntimeException("롤백", e);
            }
        }
    }

    @Transactional
    public void deleteProgression(Long progressionId) {
        Progression progression = progressionRepository.findOne(progressionId);

        if (progression.getAudioUrl() != null && !progression.getAudioUrl().isEmpty()) {
            FileUtils.deleteFile(progression.getAudioUrl());
        }

        if (progression.getSampleMidiUrl() != null && !progression.getSampleMidiUrl().isEmpty()) {
            FileUtils.deleteFile(progression.getSampleMidiUrl());
        }

        progressionRepository.delete(progressionId);
    }

    public File getProgressionSampleMidi(Long progressionId) {
        Progression progression = progressionRepository.findOne(progressionId);

        return FileUtils.getFile(progression.getSampleMidiUrl());
    }

    public Long getRandomCadenceProgressionIdWithSampleMidi() {
        List<Progression> progressions = progressionRepository.findAllCadenceProgressionsWithSampleMidi();

        if (progressions.isEmpty()) {
            throw new IllegalStateException("No progressions found in the repository");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(progressions.size());
        return progressions.get(randomIndex).getId();
    }
}
