package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.*;
import com.memoire.gestionrh.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // RH enregistre un utilisateur
     @PostMapping("/register")
     @PreAuthorize("hasRole('ROLE_RH')") // Seul le RH peut créer des comptes
     public ResponseEntity<String> register(@RequestBody RegisterDTO request) {
    return ResponseEntity.ok(authService.enregistrerUtilisateur(request));
}

    // Connexion
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO request) {
        return ResponseEntity.ok(authService.connecter(request));
    }

    // Changer mot de passe
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody NewPasswordDTO request) {
        return ResponseEntity.ok(authService.changerMotDePasse(request));
    }
}
