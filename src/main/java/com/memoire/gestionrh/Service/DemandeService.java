package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.DemandeDTO;
import com.memoire.gestionrh.DTO.ValidationDTO;
import com.memoire.gestionrh.Models.*;
import com.memoire.gestionrh.Repository.*;
import com.memoire.gestionrh.enums.StatutDemande;
import com.memoire.gestionrh.enums.StatutValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DemandeService {

    private final DemandeRepository demandeRepository;
    private final UtilisateursRepository utilisateurRepository;
    private final HistoriqueValidationRepository validationRepository;
    private final NotificationService notificationService;

    // ── Employé soumet une demande ──
    public Demande soumettredemande(DemandeDTO dto) {

        Utilisateur employe = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Demande demande = new Demande();
        demande.setTypeDemande(dto.getTypeDemande());
        demande.setDateDebut(dto.getDateDebut());
        demande.setDateFin(dto.getDateFin());
        demande.setMotif(dto.getMotif());
        demande.setDateDemande(LocalDateTime.now());
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setUtilisateur(employe);

        Demande saved = demandeRepository.save(demande);

        String role = employe.getRole().getNom().toLowerCase(); // ← normalisation unique

        switch (role) {

            case "employe" -> {
                // Notifie le responsable du service de l'employé
                if (employe.getService() != null) {
                    utilisateurRepository
                            .findByService_IdAndRole_Nom(
                                    employe.getService().getId(),
                                    "responsable") // ← UUID utilisateur, pas service
                            .ifPresent(responsable ->
                                    notificationService.envoyerNotification(
                                            responsable.getId(),
                                            "Nouvelle demande de "
                                                    + employe.getNom() + " "
                                                    + employe.getPrenom()
                                                    + " : " + dto.getTypeDemande()
                                                    + " du " + dto.getDateDebut()
                                                    + " au " + dto.getDateFin()));
                }
            }

            case "chef département" -> {
                // Notifie le chef de département
                utilisateurRepository
                        .findByService_Departement_IdAndRole_Nom(
                                employe.getService().getDepartement().getId(),
                                "Chef département")
                        .ifPresent(chef ->
                                notificationService.envoyerNotification(
                                        chef.getId(),
                                        "Nouvelle demande du Responsable "
                                                + employe.getNom() + " "
                                                + employe.getPrenom()
                                                + " : " + dto.getTypeDemande()
                                                + " du " + dto.getDateDebut()
                                                + " au " + dto.getDateFin()));
            }

            case "Chef département" -> {
                // Notifie tous les RH
                utilisateurRepository.findByRole_Nom("rh")
                        .forEach(rh ->
                                notificationService.envoyerNotification(
                                        rh.getId(),
                                        "Nouvelle demande du Chef de département "
                                                + employe.getNom() + " "
                                                + employe.getPrenom()
                                                + " : " + dto.getTypeDemande()
                                                + " du " + dto.getDateDebut()
                                                + " au " + dto.getDateFin()));
            }

            case "RH" -> {
                // Le RH est le niveau final : demande auto-approuvée
                demande.setStatut(StatutDemande.APPROUVEE_RH);
                demandeRepository.save(demande);

                HistoriqueValidation validation = new HistoriqueValidation();
                validation.setDemande(saved);
                validation.setValidateur(employe);
                validation.setStatut(StatutValidation.APPROUVE);
                validation.setDateValidation(LocalDateTime.now());
                validationRepository.save(validation);

                notificationService.envoyerNotification(
                        employe.getId(),
                        "✓ Votre demande de "
                                + dto.getTypeDemande()
                                + " du " + dto.getDateDebut()
                                + " au " + dto.getDateFin()
                                + " a été approuvée automatiquement.");
            }

            default -> throw new RuntimeException("Role inconnu : " + employe.getRole().getNom());
        }

        return saved;
    }

    // ── Validation par le Responsable direct ──
    public Demande validerParResponsable(UUID demandeId, ValidationDTO dto) {

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        Utilisateur validateur = utilisateurRepository.findById(dto.getValidateurId())
                .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));

        HistoriqueValidation validation = new HistoriqueValidation();
        validation.setDemande(demande);
        validation.setValidateur(validateur);
        validation.setStatut(dto.getStatut());
        validation.setDateValidation(LocalDateTime.now());
        validationRepository.save(validation);

        if (dto.getStatut() == StatutValidation.APPROUVE) {

            demande.setStatut(StatutDemande.APPROUVEE_RESPONSABLE);
            demandeRepository.save(demande);

            utilisateurRepository
                    .findByService_Departement_IdAndRole_Nom(
                            demande.getUtilisateur().getService().getDepartement().getId(),
                            "chefdepartement")
                    .ifPresent(chef ->
                            notificationService.envoyerNotification(
                                    chef.getId(),
                                    "La demande de "
                                            + demande.getUtilisateur().getNom() + " "
                                            + demande.getUtilisateur().getPrenom()
                                            + " a été approuvée par son responsable direct."
                                            + " En attente de votre validation."));
        } else {

            demande.setStatut(StatutDemande.REFUSEE_RESPONSABLE);
            demandeRepository.save(demande);

            notificationService.envoyerNotification(
                    demande.getUtilisateur().getId(),
                    "✗ Votre demande de "
                            + demande.getTypeDemande()
                            + " a été refusée par votre responsable direct.");
        }

        return demande;
    }

    // ── Validation par le Chef de département ──
    public Demande validerParChefDepartement(UUID demandeId, ValidationDTO dto) {

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        Utilisateur validateur = utilisateurRepository.findById(dto.getValidateurId())
                .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));

        HistoriqueValidation validation = new HistoriqueValidation();
        validation.setDemande(demande);
        validation.setValidateur(validateur);
        validation.setStatut(dto.getStatut());
        validation.setDateValidation(LocalDateTime.now());
        validationRepository.save(validation);

        if (dto.getStatut() == StatutValidation.APPROUVE) {

            demande.setStatut(StatutDemande.APPROUVEE_CHEF_DEPARTEMENT);
            demandeRepository.save(demande);

            utilisateurRepository.findByRole_Nom("rh")
                    .forEach(rh ->
                            notificationService.envoyerNotification(
                                    rh.getId(),
                                    "La demande de "
                                            + demande.getUtilisateur().getNom() + " "
                                            + demande.getUtilisateur().getPrenom()
                                            + " a été approuvée par le chef de département."
                                            + " En attente de votre validation."));
        } else {

            demande.setStatut(StatutDemande.REFUSEE_CHEF_DEPARTEMENT);
            demandeRepository.save(demande);

            notificationService.envoyerNotification(
                    demande.getUtilisateur().getId(),
                    "✗ Votre demande de "
                            + demande.getTypeDemande()
                            + " a été refusée par le chef de département.");
        }

        return demande;
    }

    // ── Validation finale par le RH ──
    public Demande validerParRH(UUID demandeId, ValidationDTO dto) {

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        Utilisateur validateur = utilisateurRepository.findById(dto.getValidateurId())
                .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));

        HistoriqueValidation validation = new HistoriqueValidation();
        validation.setDemande(demande);
        validation.setValidateur(validateur);
        validation.setStatut(dto.getStatut());
        validation.setDateValidation(LocalDateTime.now());
        validationRepository.save(validation);

        if (dto.getStatut() == StatutValidation.APPROUVE) {

            demande.setStatut(StatutDemande.APPROUVEE_RH);
            demandeRepository.save(demande);

            notificationService.envoyerNotification(
                    demande.getUtilisateur().getId(),
                    "✓ Votre demande de "
                            + demande.getTypeDemande()
                            + " du " + demande.getDateDebut()
                            + " au " + demande.getDateFin()
                            + " a été approuvée définitivement par le RH !");
        } else {

            demande.setStatut(StatutDemande.REFUSEE_RH);
            demandeRepository.save(demande);

            notificationService.envoyerNotification(
                    demande.getUtilisateur().getId(),
                    "✗ Votre demande de "
                            + demande.getTypeDemande()
                            + " a été refusée par le RH.");
        }

        return demande;
    }

    // ── Récupérer toutes les demandes ──
    public List<Demande> getToutesLesDemandes() {
        return demandeRepository.findAll();
    }

    // ── Récupérer les demandes d'un utilisateur ──
    public List<Demande> getDemandesParUtilisateur(UUID utilisateurId) {
        return demandeRepository.findByUtilisateurId(utilisateurId);
    }

    // ── Récupérer les demandes en attente ──
    public List<Demande> getDemandesEnAttente() {
        return demandeRepository.findByStatut(StatutDemande.EN_ATTENTE);
    }

    // ── Supprimer une demande ──
    public void supprimerDemande(UUID id) {
        demandeRepository.deleteById(id);
    }
}