package jhhan.harmonynow_backend.controller;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChordController {

    /* 관리자 전용 */
    @GetMapping("/admin/chords")
    public String adminChordList(Model model) {
        return "/admin/chordList";
    }
}
