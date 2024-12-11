/**
 * AdminInterceptor
 *
 * [1. 역할]
 * - 관리자 페이지 요청에 대해 인증 여부를 확인하는 인터셉터.
 * - 관리자로 인증되지 않은 사용자의 요청을 차단하고 로그인 페이지로 리다이렉트.
 *
 * [2. 주요 기능]
 * - HTTP 요청의 세션에서 `isAdmin` 속성을 확인하여 인증 여부 판단.
 * - 인증되지 않은 경우 `/admin` 경로로 리다이렉트 처리.
 * - 인증된 경우 요청을 계속 진행.
 *
 * [3. 사용 사례]
 * - 관리자 페이지(`/admin/**`)에 접근하는 모든 요청에 대해 사전 인증 검사를 수행.
 */


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
