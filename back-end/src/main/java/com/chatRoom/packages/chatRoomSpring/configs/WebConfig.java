package com.chatRoom.packages.chatRoomSpring.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Autoriser les requêtes sur les routes commençant par /api/
                .allowedOrigins("http://localhost:3000") // Autoriser le front-end React sur localhost:3000
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Méthodes autorisées
                .allowCredentials(true) // Permettre l'envoi de cookies ou d'informations d'identification
                .allowedHeaders("*") // Autoriser tous les en-têtes
                .maxAge(3600); // Durée pendant laquelle les résultats CORS sont mis en cache
    }
}
