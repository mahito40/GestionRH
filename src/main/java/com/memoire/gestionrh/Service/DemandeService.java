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

                String poste = employe.getPoste();

                switch (poste) {

                        case "Employe" -> {
                                // Notification au responsable du service si l'employe a un service
                                if (employe.getService() != null) {
                                        notificationService.envoyerNotification(
                                                        employe.getService().getId(),
                                                        "Nouvelle demande de "
                                                                        + employe.getNom() + " "
                                                                        + employe.getPrenom()
                                                                        + " : " + dto.getTypeDemande()
                                                                        + " du " + dto.getDateDebut()
                                                                        + " au " + dto.getDateFin());
                                }
                        }

                        case "Responsable" -> {

                                utilisateurRepository
                                                .findByService_Departement_IdAndPoste(
                                                                employe.getService().getDepartement().getId(),
                                                                "ChefDepartement")

                                                .ifPresent(chef -> notificationService.envoyerNotification(

                                                                chef.getId(),

                                                                "Nouvelle demande du Responsable "
                                                                                + employe.getNom()
                                                                                + " "
                                                                                + employe.getPrenom()
                                                                                + " : "
                                                                                + dto.getTypeDemande()
                                                                                + " du "
                                                                                + dto.getDateDebut()
                                                                                + " au "
                                                                                + dto.getDateFin()));
                        }

                        case "ChefDepartement" -> {

                                utilisateurRepository.findByPoste("RH")

                                                .forEach(rh -> notificationService.envoyerNotification(

                                                                rh.getId(),

                                                                "Nouvelle demande du Chef de département "
                                                                                + employe.getNom()
                                                                                + " "
                                                                                + employe.getPrenom()
                                                                                + " : "
                                                                                + dto.getTypeDemande()
                                                                                + " du "
                                                                                + dto.getDateDebut()
                                                                                + " au "
                                                                                + dto.getDateFin()));
                        }

                        case "RH" -> {

                                utilisateurRepository.findByPoste("DG")

                                                .forEach(dg -> notificationService.envoyerNotification(

                                                                dg.getId(),

                                                                "Nouvelle demande du RH "
                                                                                + employe.getNom()
                                                                                + " "
                                                                                + employe.getPrenom()
                                                                                + " : "
                                                                                + dto.getTypeDemande()
                                                                                + " du "
                                                                                + dto.getDateDebut()
                                                                                + " au "
                                                                                + dto.getDateFin()));
                        }

                        case "DG" -> {

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
                                                                + " du "
                                                                + dto.getDateDebut()
                                                                + " au "
                                                                + dto.getDateFin()
                                                                + " a été approuvée automatiquement.");
                        }

                        default -> throw new RuntimeException("Poste inconnu : " + poste);
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

                        // notification au chef de département

                        utilisateurRepository

                                        .findByService_Departement_IdAndPoste(

                                                        demande.getUtilisateur()
                                                                        .getService()
                                                                        .getDepartement()
                                                                        .getId(),

                                                        "ChefDepartement")

                                        .ifPresent(chef -> notificationService.envoyerNotification(

                                                        chef.getId(),

                                                        "La demande de "
                                                                        + demande.getUtilisateur().getNom()
                                                                        + " "
                                                                        + demande.getUtilisateur().getPrenom()
                                                                        + " a été approuvée par son responsable direct."
                                                                        + " En attente de votre validation."));

                } else {

                        demande.setStatut(StatutDemande.REFUSEE_CHEF_DEPARTEMENT);

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

                        utilisateurRepository.findByPoste("RH")

                                        .forEach(rh -> notificationService.envoyerNotification(

                                                        rh.getId(),

                                                        "La demande de "
                                                                        + demande.getUtilisateur().getNom()
                                                                        + " "
                                                                        + demande.getUtilisateur().getPrenom()
                                                                        + " a été approuvée par le chef de département."
                                                                        + " En attente de votre validation."));

                } else {

                        demande.setStatut(StatutDemande.REFUSEE_RH);

                        demandeRepository.save(demande);

                        notificationService.envoyerNotification(

                                        demande.getUtilisateur().getId(),

                                        "✗ Votre demande de "
                                                        + demande.getTypeDemande()
                                                        + " a été refusée par le chef de département.");
                }

                return demande;
        }

        // ── Validation par le RH ──
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
                                                        + " du "
                                                        + demande.getDateDebut()
                                                        + " au "
                                                        + demande.getDateFin()
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