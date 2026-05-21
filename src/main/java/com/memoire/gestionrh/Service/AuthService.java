package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.*;
import com.memoire.gestionrh.Models.Role;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.memoire.gestionrh.Security.JwtUtils;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateursRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // ── Connexion ──
    // ── Connexion ── retourne le JWT
    public AuthResponseDTO connecter(LoginDTO request) {

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email manquant.");
        }

        if (request.getMotDePasse() == null || request.getMotDePasse().isBlank()) {
            throw new RuntimeException("Mot de passe manquant.");
        }

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email incorrect."));

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect.");
        }

        // Génération du token
        String roleName = utilisateur.getRole() != null ? utilisateur.getRole().getNom() : null;
        String token = jwtUtils.generateToken(
                utilisateur.getEmail(),
                utilisateur.getPoste(),
                roleName);

        RoleResponseDTO role = null;
        Role roleEntity = utilisateur.getRole();
        if (roleEntity != null) {
            role = new RoleResponseDTO(roleEntity.getId(), roleEntity.getNom(), roleEntity.getDescription());
        }

        UUID departementId = null;
        if (utilisateur.getService() != null && utilisateur.getService().getDepartement() != null) {
            departementId = utilisateur.getService().getDepartement().getId();
        }

        // Retour des données
        return new AuthResponseDTO(
                token,
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getPoste(),
                departementId,
                Boolean.valueOf(utilisateur.getIsfirstlogin()),
                role);
    }

    // Route unique: si ancien mot de passe est absent, on autorise uniquement la
    // premiere connexion.
    public String changerMotDePasse(String authenticatedEmail, NewPasswordDTO request) {
        if (authenticatedEmail == null || authenticatedEmail.isBlank()) {
            throw new RuntimeException("Utilisateur non authentifie.");
        }
        if (request.getNouveauMotDePasse() == null || request.getNouveauMotDePasse().isBlank()) {
            throw new RuntimeException("Nouveau mot de passe manquant.");
        }

        Utilisateur utilisateur = utilisateurRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        boolean ancienMotDePasseFourni = request.getAncienMotDePasse() != null
                && !request.getAncienMotDePasse().isBlank();

        if (ancienMotDePasseFourni) {
            if (!passwordEncoder.matches(request.getAncienMotDePasse(), utilisateur.getMotDePasse())) {
                throw new RuntimeException("Ancien mot de passe incorrect.");
            }
        } else if (!Boolean.TRUE.equals(utilisateur.getIsfirstlogin())) {
            throw new RuntimeException("Ancien mot de passe requis.");
        }

        // Met à jour le mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateur.setIsfirstlogin(false); // plus besoin de changer le mot de passe à la prochaine connexion
        utilisateurRepository.save(utilisateur);

        return "Mot de passe changé avec succès.";
    }
}