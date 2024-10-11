package jhhan.harmonynow_backend.controller;


import jakarta.validation.Valid;
import jhhan.harmonynow_backend.dto.CreateChordDTO;
import jhhan.harmonynow_backend.service.ChordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChordController {

    private final ChordService chordService;

    /* 관리자 전용 */
    @GetMapping("/admin/chords")
    public String adminChordList(Model model) {
        return "admin/chordList";
    }

    @GetMapping("/admin/chords/new")
    public String adminCreateChordForm(Model model) {
        model.addAttribute("form", new CreateChordDTO());
        return "admin/createChordForm";
    }

    @PostMapping("/admin/chords/new")
    public String adminCreateChord(@Valid @ModelAttribute("form") CreateChordDTO createChordDTO,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()){
            return "admin/createChordForm";
        }

        chordService.saveChord(createChordDTO);
        return "redirect:/admin/chords";
    }
}
