package com.xclone.xclone.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5174") // adjust to your React dev origin
                        .allowedMethods("*") // includes OPTIONS
                        .allowedHeaders("*") // allow any headers
                        .allowCredentials(true); // if you use cookies or Authorization header
            }
        };
    }
}