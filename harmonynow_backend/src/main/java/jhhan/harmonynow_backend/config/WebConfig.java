/**
 * WebConfig
 *
 * [1. 역할]
 * - Spring MVC의 웹 애플리케이션 설정을 담당하는 클래스.
 *
 * [2. 주요 기능]
 * - 정적 리소스 핸들링:
 *   - 업로드된 파일이 `/uploads/**` 경로로 접근 가능하도록 설정.
 *   - 파일 시스템의 특정 경로(`targetBasePath`)를 매핑.
 * - 인터셉터 등록:
 *   - `AdminInterceptor`를 등록하여 `/admin/**` 경로에 대해 인증 및 권한 처리를 수행.
 *   - 로그인 및 인증이 필요하지 않은 경로는 제외.
 *
 * [3. 사용 사례]
 * - 사용자가 업로드된 파일에 접근하거나, 관리자 페이지의 요청을 처리할 때 경로와 권한을 관리.
 */


package jhhan.harmonynow_backend.config;

import jhhan.harmonynow_backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.target-base-path}")
    private String targetBasePath;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + targetBasePath + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin", "/admin/login");
    }
}
