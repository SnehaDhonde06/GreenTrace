package com.greentrace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static resources from classpath:/static/
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);

        // Serve uploaded files
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCachePeriod(0);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Simple view controllers - removed problematic pattern
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/index").setViewName("forward:/index.html");
        registry.addViewController("/home").setViewName("forward:/home.html");
        registry.addViewController("/dashboard").setViewName("forward:/dashboard.html");
        registry.addViewController("/profile").setViewName("forward:/profile.html");
        registry.addViewController("/login").setViewName("forward:/login.html");
        registry.addViewController("/signup").setViewName("forward:/signup.html");
        registry.addViewController("/foodPrev").setViewName("forward:/foodPrev.html");
        registry.addViewController("/recipes").setViewName("forward:/recipes.html");
        registry.addViewController("/healthtips").setViewName("forward:/healthtips.html");
        registry.addViewController("/foodwastetips").setViewName("forward:/foodwastetips.html");
    }
}