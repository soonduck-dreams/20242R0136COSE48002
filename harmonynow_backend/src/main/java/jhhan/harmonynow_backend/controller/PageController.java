/**
 * PageController
 *
 * [1. 역할]
 * - 페이지 탐색을 처리하는 컨트롤러.
 * - 메인 페이지, 관리자 로그인 페이지 및 학습하기 페이지로의 이동을 처리.
 *
 * [2. 주요 기능]
 * - 메인 페이지(index)로의 요청 처리.
 * - 관리자 페이지 로그인 처리 및 인증 코드 확인.
 * - 학습하기 페이지로 리디렉션 처리.
 *
 * [3. 사용 사례]
 * - 사용자가 애플리케이션의 메인 페이지나 학습하기 페이지를 탐색하거나, 관리자로 로그인할 때 사용.
 */


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
