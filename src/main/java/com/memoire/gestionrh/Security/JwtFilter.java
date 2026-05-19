package com.memoire.gestionrh.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        logger.debug("[JwtFilter] Authorization header: {}", authHeader != null ? "present" : "null");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.debug("[JwtFilter] Token extracted, validating...");

            if (jwtUtils.validateToken(token)) {
                String email = jwtUtils.getEmailFromToken(token);
                String role = jwtUtils.getRoleFromToken(token);

                logger.debug("[JwtFilter] Token valid! Email: {}, Role: {}", email, role);

                // Utilise le rôle du token au lieu du poste
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + (role != null ? role.toUpperCase() : "USER"))));

                logger.debug("[JwtFilter] Authentication set with authority: ROLE_{}",
                        role != null ? role.toUpperCase() : "USER");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("[JwtFilter] Token validation failed!");
            }
        } else {
            logger.debug("[JwtFilter] No Bearer token in request");
        }

        filterChain.doFilter(request, response);
    }
}
