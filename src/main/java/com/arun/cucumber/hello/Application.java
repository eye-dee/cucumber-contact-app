package com.arun.cucumber.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        // .allowedOrigins("http://domain2.com")
                        .allowedMethods("PUT", "DELETE", "GET", "POST")
                        // .allowedHeaders("header1", "header2", "header3")
                        // .exposedHeaders("header1", "header2")
                        .allowCredentials(false).maxAge(3600);
            }
        };
    }

}
