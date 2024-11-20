package jhhan.harmonynow_backend.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    @Value("${admin.code}")
    private String adminCode;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/admin")
    public String adminPage(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "redirect:/admin/chords";
        }

        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String code, HttpSession session, Model model) {
        if (adminCode.equals(code)) {
            session.setAttribute("isAdmin", true);
            return "redirect:/admin";
        }

        model.addAttribute("error", "인증 코드가 틀렸습니다. 다시 시도해주세요.");
        return "admin/login";
    }

    @GetMapping("/learn")
    public String learnPage() {
        return "redirect:/learn/chords?level=beginner";
    }
}
