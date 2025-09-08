package com.example.demo.config;

// import com.example.demo.interceptor.ApiKeyAuthInterceptor;
import com.example.demo.interceptor.ApiKeyAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiKeyAuthInterceptor apiKeyAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the ApiKeyAuthInterceptor
        registry.addInterceptor(apiKeyAuthInterceptor)
                // Secure all OTP-related endpoints
                .addPathPatterns("/api/v1/otp/**");
    }
}