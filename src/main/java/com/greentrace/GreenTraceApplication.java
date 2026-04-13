package com.greentrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class GreenTraceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenTraceApplication.class, args);
        System.out.println("🚀 Green Trace Backend Started Successfully!");
        System.out.println("📝 API Documentation: http://localhost:8082  /api/swagger-ui.html");
        System.out.println("🌱 Green Trace - Food Waste Prevention Application");
    }
}