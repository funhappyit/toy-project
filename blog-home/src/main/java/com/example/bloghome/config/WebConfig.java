package com.example.bloghome.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
*WebConfig 클래스는 Spring Boot 애플리케이션에서 정적 자원(파일)을 서빙하기 위한 설정을 정의하고 있습니다.
*  addResourceHandlers 메소드는 요청 경로와 파일 시스템의 실제 경로를 매핑하는 역할
* */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/Users/han-yugyeong/Desktop/study/uploads/");
    }
}
