package com.memoire.gestionrh.Config;

import com.memoire.gestionrh.Security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // ← permet @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtFilter jwtFilter;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                // ── Preflights CORS ──
                                                .requestMatchers(HttpMethod.OPTIONS, "/**")
                                                .permitAll()
                                                // ── Public ──
                                                .requestMatchers(HttpMethod.PUT,
                                                "/{id}/valider-responsable")
                                                .hasAuthority("responsable")

                                                 .requestMatchers(HttpMethod.PUT,
                                                  "/{id}/valider-chef-departement")
                                                .hasAuthority("Chef département")

                                                 .requestMatchers(HttpMethod.PUT,
                                                 "/{id}/valider-rh")
                                                .hasAuthority("RH")
                                                .requestMatchers("/api/auth/login").permitAll()
                                                .requestMatchers("/api/auth/change-password").authenticated()
                                                .requestMatchers("/ws/**").permitAll()
                                                // ── Swagger ──
                                                .requestMatchers(
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/v3/api-docs/**",
                                                                "/swagger-resources/**",
                                                                "/webjars/**")
                                                .permitAll()
                                                // ── Tout le reste → authentifié ──
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                                

                return http.build();
        }
}