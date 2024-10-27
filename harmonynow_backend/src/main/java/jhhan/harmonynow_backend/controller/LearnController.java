package jhhan.harmonynow_backend.controller;

import jhhan.harmonynow_backend.service.ChordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LearnController {

    private final ChordService chordService;

    @GetMapping("/learn/chords")
    public String chordListPage(@RequestParam(required = false) String level, Model model) {
        if (level == null) {
            return "redirect:/learn/chords?level=beginner";
        }

        model.addAttribute("level", level);
        model.addAttribute("chordDtoList", chordService.findPublicChordsByLevel(level));

        return "learn/chordList";
    }
}
