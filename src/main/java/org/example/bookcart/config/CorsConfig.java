package org.example.bookcart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config =
                new CorsConfiguration();

        // ALLOWED FRONTEND URLS

        config.setAllowedOrigins(
                List.of(

                        "http://localhost:5173",

                        "https://booknest-frontend-silk.vercel.app"

                )
        );

        // ALLOWED METHODS

        config.setAllowedMethods(
                List.of(

                        "GET",

                        "POST",

                        "PUT",

                        "DELETE",

                        "OPTIONS"
                )
        );

        // ALLOWED HEADERS

        config.setAllowedHeaders(
                List.of("*")
        );

        // ALLOW JWT / COOKIES

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                config
        );

        return new CorsFilter(source);
    }
}