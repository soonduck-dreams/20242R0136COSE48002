package jhhan.harmonynow_backend.service;

import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.dto.CreateChordDTO;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChordService {

    private final ChordRepository chordRepository;

    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    @Transactional
    public void saveChord(CreateChordDTO createChordDTO) {
        Chord chord = Chord.createChord(createChordDTO);
        Long savedId = chordRepository.save(chord);

        if (createChordDTO.getImageFile() != null && !createChordDTO.getImageFile().isEmpty()) {
            try {
                String imageUrl = FileUtils.saveImage(createChordDTO.getImageFile(), "chord/image", savedId);
                chord.setImageUrl(imageUrl);
            } catch (Exception e) {
                logger.error("이미지 파일 저장 실패: id = " + savedId, e);
            }
        } else {
            System.out.println("이미지 업로드하지 않음");
        }

        if (createChordDTO.getAudioFile() != null && !createChordDTO.getAudioFile().isEmpty()) {
            try {
                String audioUrl = FileUtils.saveAudio(createChordDTO.getAudioFile(), "chord/audio", savedId);
                chord.setAudioUrl(audioUrl);
            } catch (Exception e) {
                logger.error("오디오 파일 저장 실패: id = " + savedId, e);
            }
        } else {
            System.out.println("오디오 업로드하지 않음");
        }
    }
}
