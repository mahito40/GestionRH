package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.*;
import com.memoire.gestionrh.Models.ServiceEN;
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
    ServiceEN service = serviceRepository.findById(request.getServiceId())
            .orElseThrow(() -> new RuntimeException("Service introuvable"));

    // 4. Créer utilisateur
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setNom(request.getNom());
    utilisateur.setPrenom(request.getPrenom());
    utilisateur.setEmail(request.getEmail());
    utilisateur.setPoste(request.getPoste());
    utilisateur.setService(service);
    utilisateur.setMotDePasse(passwordEncoder.encode(codeProvisoire));
    utilisateur.setDoitChangerMotDePasse(true);
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
public String connecter(LoginDTO request) {
    Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Email incorrect."));

    if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
        throw new RuntimeException("Mot de passe incorrect.");
    }

    if (utilisateur.getDoitChangerMotDePasse()) {
        return "DOIT_CHANGER_MOT_DE_PASSE";
    }

    // ← génère et retourne le JWT avec le poste
    return jwtUtils.generateToken(utilisateur.getEmail(), utilisateur.getPoste());
}
    // ── Changer le mot de passe ──
    public String changerMotDePasse(NewPasswordDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        if (!passwordEncoder.matches(request.getAncienMotDePasse(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("Code provisoire incorrect.");
        }

        // Met à jour le mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateur.setDoitChangerMotDePasse(false); // plus besoin de changer
        utilisateurRepository.save(utilisateur);

        return "Mot de passe changé avec succès.";
    }
}