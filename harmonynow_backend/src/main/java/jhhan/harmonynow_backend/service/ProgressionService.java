package jhhan.harmonynow_backend.service;

import jakarta.validation.Valid;
import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.domain.ChordProgressionMap;
import jhhan.harmonynow_backend.domain.Progression;
import jhhan.harmonynow_backend.dto.CreateProgressionDTO;
import jhhan.harmonynow_backend.repository.ChordProgressionMapRepository;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.repository.ProgressionRepository;
import jhhan.harmonynow_backend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
