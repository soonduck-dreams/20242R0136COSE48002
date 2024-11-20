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
                .addResourceLocations("file:///" + targetBasePath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin", "/admin/login");
    }
}
