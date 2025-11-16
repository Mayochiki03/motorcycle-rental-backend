package com.temptation.motorcyclerental.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // สำหรับ serve ไฟล์ที่อัพโหลด
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}