package jhhan.harmonynow_backend.service;

import jakarta.validation.Valid;
import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.domain.ChordProgressionMap;
import jhhan.harmonynow_backend.domain.Progression;
import jhhan.harmonynow_backend.dto.ChordNameIdDTO;
import jhhan.harmonynow_backend.dto.CreateProgressionDTO;
import jhhan.harmonynow_backend.dto.EditProgressionDTO;
import jhhan.harmonynow_backend.repository.ChordProgressionMapRepository;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.repository.ProgressionRepository;
import jhhan.harmonynow_backend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgressionService {

    private final ProgressionRepository progressionRepository;
    private final ChordRepository chordRepository;
    private final ChordProgressionMapRepository mapRepository;

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

    public List<Long> getChordIdsInProgression(Progression progression) {
        List<ChordNameIdDTO> dtoList = chordRepository.findChordNameIdByProgressionId(progression.getId());

        List<Long> ids = new ArrayList<>();
        for (ChordNameIdDTO dto : dtoList) {
            ids.add(dto.getId());
        }

        return ids;
    }

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
}
