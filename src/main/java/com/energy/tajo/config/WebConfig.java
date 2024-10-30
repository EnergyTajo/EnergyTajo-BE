package com.energy.tajo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("https://www.energytajo.site", "http://localhost:3000", "http://192.168.45.106:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .exposedHeaders("Authorization")
            .allowCredentials(true);
    }
}
