package jhhan.harmonynow_backend.controller;

import jhhan.harmonynow_backend.dto.ReadChordDTO;
import jhhan.harmonynow_backend.dto.ReadProgressionDTO;
import jhhan.harmonynow_backend.repository.ChordProgressionMapRepository;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.service.ChordService;
import jhhan.harmonynow_backend.service.ProgressionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LearnController {

    private final ChordService chordService;
    private final ChordRepository chordRepository;
    private final ProgressionService progressionService;

    @GetMapping("/learn/chords")
    public String chordList(@RequestParam(required = false) String level, Model model) {
        if (level == null) {
            return "redirect:/learn/chords?level=beginner";
        }

        model.addAttribute("level", level);
        model.addAttribute("chordDtoList", chordService.findPublicChordsByLevel(level));

        return "learn/chordList";
    }

    @GetMapping("/learn/chords/{chordId}")
    public String chordDetail(@PathVariable Long chordId, Model model) {
        ReadChordDTO dto = chordService.findChordById(chordId);
        List<ReadProgressionDTO> progressionDTOList = progressionService.getProgressionsByChordId(chordId);

        model.addAttribute("chord", dto);
        model.addAttribute("progressions", progressionDTOList);

        return "learn/chordDetail";
    }

    @GetMapping("/learn/progressions/{progressionId}")
    public String progressionDetail(@PathVariable Long progressionId, Model model) {
        ReadProgressionDTO dto = progressionService.getProgressionById(progressionId, false);
        List<ReadChordDTO> chordDTOList = chordRepository.findChordsByProgressionId(progressionId);

        model.addAttribute("progression", dto);
        model.addAttribute("chords", chordDTOList);

        return "learn/progressionDetail";
    }
}
