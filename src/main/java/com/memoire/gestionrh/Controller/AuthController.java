package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.*;
import com.memoire.gestionrh.Service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Connexion
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO request) {
        return ResponseEntity.ok(authService.connecter(request));
    }

    // Route unique de changement du mot de passe (premiere connexion et cas
    // standard)
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody NewPasswordDTO request,
            Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(authService.changerMotDePasse(email, request));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
    return ResponseEntity.ok("Déconnexion réussie");
}
}
