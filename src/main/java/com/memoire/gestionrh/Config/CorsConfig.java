package com.memoire.gestionrh.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**")

                        // Angular (port par défaut)
                        .allowedOrigins("http://localhost:4200")

                        // Autoriser toutes les méthodes HTTP
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                        // Autoriser les headers (important pour JWT)
                        .allowedHeaders("*")

                        // Autoriser l'envoi de token JWT
                        .allowCredentials(true);
            }
        };
    }
}