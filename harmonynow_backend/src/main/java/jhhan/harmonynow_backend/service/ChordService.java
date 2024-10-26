package jhhan.harmonynow_backend.service;

import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.dto.CreateChordDTO;
import jhhan.harmonynow_backend.dto.EditChordDTO;
import jhhan.harmonynow_backend.exception.ChordNotDeletableException;
import jhhan.harmonynow_backend.repository.ChordProgressionMapRepository;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChordService {

    private final ChordRepository chordRepository;
    private final ChordProgressionMapRepository mapRepository;

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
}