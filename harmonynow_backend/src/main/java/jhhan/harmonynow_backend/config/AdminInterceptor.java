package jhhan.harmonynow_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");

        if (isAdmin == null || !isAdmin) {
            response.sendRedirect("/admin");
            return false; // 인증 페이지로 리다이렉트
        }
        return true; // 통과
    }
}
