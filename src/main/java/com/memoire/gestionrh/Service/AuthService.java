package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.*;
import com.memoire.gestionrh.Models.service;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import com.memoire.gestionrh.Repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.memoire.gestionrh.Security.JwtUtils;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateursRepository utilisateurRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ServiceRepository serviceRepository;
    
    public String enregistrerUtilisateur(RegisterDTO request) {

    // 1. Vérifier email
    if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new RuntimeException("Un compte existe déjà avec cet email.");
    }

    // 2. Générer code provisoire
    String codeProvisoire = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    // 3. Récupérer service
    service service = serviceRepository.findById(request.getServiceId())
            .orElseThrow(() -> new RuntimeException("Service introuvable"));

            
    // 4. Créer utilisateur
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setNom(request.getNom());
    utilisateur.setPrenom(request.getPrenom());
    utilisateur.setEmail(request.getEmail());
    utilisateur.setPoste(request.getPoste());
    utilisateur.setService(service);
    utilisateur.setMotDePasse(passwordEncoder.encode(codeProvisoire));
    utilisateur.setIsfirstlogin(true);
        utilisateurRepository.save(utilisateur);

        // Envoie le mail avec le code provisoire
        emailService.envoyerCodeProvisoire(
            request.getEmail(),
            request.getNom(),
            codeProvisoire
        );

        return "Utilisateur créé avec succès. Un mail a été envoyé à " + request.getEmail();
    }

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

    if (utilisateur.getIsfirstlogin()) {
        throw new RuntimeException("DOIT_CHANGER_MOT_DE_PASSE");
    }

    // Génération du token
    String token = jwtUtils.generateToken(
            utilisateur.getEmail(),
            utilisateur.getPoste()
    );

    // Retour des données
    return new AuthResponseDTO(
            token,
            utilisateur.getId(),
            utilisateur.getNom(),
            utilisateur.getPrenom(),
            utilisateur.getEmail(),
            utilisateur.getPoste()
    );
}
    // ── Changer le mot de passe ──
    public String changerMotDePasse(NewPasswordDTO request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email manquant.");
        }
        if (request.getAncienMotDePasse() == null || request.getAncienMotDePasse().isBlank()) {
            throw new RuntimeException("Ancien mot de passe manquant.");
        }
        if (request.getNouveauMotDePasse() == null || request.getNouveauMotDePasse().isBlank()) {
            throw new RuntimeException("Nouveau mot de passe manquant.");
        }

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        if (!passwordEncoder.matches(request.getAncienMotDePasse(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("Code provisoire incorrect.");
        }

        // Met à jour le mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateur.setIsfirstlogin(false); // plus besoin de changer le mot de passe à la prochaine connexion
        utilisateurRepository.save(utilisateur);

        return "Mot de passe changé avec succès.";
    }
}