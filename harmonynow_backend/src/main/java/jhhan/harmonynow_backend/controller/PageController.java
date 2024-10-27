package jhhan.harmonynow_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "redirect:/admin/chords";
    }

    @GetMapping("/learn")
    public String learnPage() {
        return "redirect:/learn/chords?level=beginner";
    }
}
