/**
 * ChordService
 *
 * [1. 역할]
 * - 화성학에서 코드(Chord, 화음)는 두 개 이상의 음이 동시에 울리는 것을 나타냄.
 * - 코드 데이터를 생성, 수정, 삭제, 조회하는 기능을 제공.
 *
 * [2. 주요 기능]
 * - 새로운 코드를 생성하고 관련 파일(이미지, 오디오)을 저장.
 * - 기존 코드를 업데이트하고 파일 삭제 또는 수정 처리.
 * - 해당 코드를 사용 중인 코드 진행이 있으면 삭제 실패 처리.
 * - 코드의 공개 상태 및 난이도(Level)에 따라 코드 목록을 조회.
 * - 특정 코드 ID로 코드 세부 정보를 조회.
 *
 * [3. 사용 사례]
 * - 관리자 페이지에서 새로운 코드를 생성하거나 기존 코드 정보를 수정/삭제.
 * - 학습하기 페이지에서 난이도별 코드 목록을 조회하거나 코드 상세 페이지에서 코드 정보를 확인.
 *
 */



package jhhan.harmonynow_backend.service;

import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.domain.Level;
import jhhan.harmonynow_backend.dto.CreateChordDTO;
import jhhan.harmonynow_backend.dto.EditChordDTO;
import jhhan.harmonynow_backend.dto.ReadChordDTO;
import jhhan.harmonynow_backend.exception.ChordNotDeletableException;
import jhhan.harmonynow_backend.repository.ChordProgressionMapRepository;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChordService {

    private final ChordRepository chordRepository;
    private final ChordProgressionMapRepository mapRepository;

    @Transactional
    public void saveChord(CreateChordDTO dto) {
        Chord chord = Chord.createChord(dto);
        Long savedId = chordRepository.save(chord);

        try {
            String imageUrl = FileUtils.saveImageIfExists(dto.getImageFile(), "chord/image", savedId);
            chord.setImageUrl(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException("롤백", e);
        }

        try {
            String audioUrl = FileUtils.saveAudioIfExists(dto.getAudioFile(), "chord/audio", savedId);
            chord.setAudioUrl(audioUrl);
        } catch (Exception e) {
            throw new RuntimeException("롤백", e);
        }
    }

    @Transactional
    public void updateChord(Long chordId, EditChordDTO dto) {
        Chord chord = chordRepository.findOne(chordId);
        chord.updateChord(dto);

        Long savedId = chordRepository.save(chord);

        // 파일 삭제 또는 수정 로직
        if (Boolean.TRUE.equals(dto.getIsImageDeleteRequested()) && FileUtils.deleteFile(chord.getImageUrl())) {
            chord.setImageUrl(null);
        } else {
            try {
                String imageUrl = FileUtils.saveImageIfExists(dto.getImageFile(), "chord/image", savedId);
                if (imageUrl != null) {
                    chord.setImageUrl(imageUrl);
                }
            } catch (Exception e) {
                throw new RuntimeException("롤백", e);
            }
        }

        if (Boolean.TRUE.equals(dto.getIsAudioDeleteRequested()) && FileUtils.deleteFile(chord.getAudioUrl())) {
            chord.setAudioUrl(null);
        } else {
            try {
                String audioUrl = FileUtils.saveAudioIfExists(dto.getAudioFile(), "chord/audio", savedId);
                if (audioUrl != null) {
                    chord.setAudioUrl(audioUrl);
                }
            } catch (Exception e) {
                throw new RuntimeException("롤백", e);
            }
        }
    }

    @Transactional
    public void deleteChord(Long chordId) {
        Chord chord = chordRepository.findOne(chordId);

        if (mapRepository.countByChordId(chordId) > 0) {
            throw new ChordNotDeletableException("삭제 실패! 현재 이 코드를 사용 중인 코드 진행이 있습니다.");
        }

        if (chord.getImageUrl() != null && !chord.getImageUrl().isEmpty()) {
            FileUtils.deleteFile(chord.getImageUrl());
        }

        if (chord.getAudioUrl() != null && !chord.getAudioUrl().isEmpty()) {
            FileUtils.deleteFile(chord.getAudioUrl());
        }

        chordRepository.delete(chordId);
    }

    public List<ReadChordDTO> findPublicChordsByLevel(String displayLevel) {
        Level level = switch (displayLevel) {
            case "beginner" -> Level.B;
            case "intermediate" -> Level.C;
            case "advanced" -> Level.D;
            default -> Level.B;
        };

        List<Chord> chordList = chordRepository.findPublicChordsByLevel(level);
        List<ReadChordDTO> dtoList = new ArrayList<>();
        for (Chord chord : chordList) {
            dtoList.add(new ReadChordDTO(chord));
        }

        return dtoList;
    }

    public ReadChordDTO findChordById(Long chordId) {
        Chord chord = chordRepository.findOne(chordId);
        return new ReadChordDTO(chord);
    }
}