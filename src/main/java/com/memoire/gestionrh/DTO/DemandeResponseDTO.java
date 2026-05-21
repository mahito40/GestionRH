package com.memoire.gestionrh.DTO;

import com.memoire.gestionrh.enums.StatutDemande;
import com.memoire.gestionrh.enums.TypeDemande;
import com.memoire.gestionrh.Models.Demande;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class DemandeResponseDTO {

    private UUID id;
    private TypeDemande typeDemande;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private StatutDemande statut;
    private LocalDateTime dateDemande;

    // Infos utilisateur aplaties
    private UUID utilisateurId;
    private String utilisateurNom;
    private String utilisateurPrenom;

    // Constructeur depuis l'entité Demande
    public static DemandeResponseDTO from(Demande demande) {
        DemandeResponseDTO dto = new DemandeResponseDTO();
        dto.id           = demande.getId();
        dto.typeDemande  = demande.getTypeDemande();
        dto.dateDebut    = demande.getDateDebut();
        dto.dateFin      = demande.getDateFin();
        dto.motif        = demande.getMotif();
        dto.statut       = demande.getStatut();
        dto.dateDemande  = demande.getDateDemande();

        if (demande.getUtilisateur() != null) {
            dto.utilisateurId     = demande.getUtilisateur().getId();
            dto.utilisateurNom    = demande.getUtilisateur().getNom();
            dto.utilisateurPrenom = demande.getUtilisateur().getPrenom();
        }

        return dto;
    }

    // Getters
    public UUID getId()                   { return id; }
    public TypeDemande getTypeDemande()   { return typeDemande; }
    public LocalDate getDateDebut()       { return dateDebut; }
    public LocalDate getDateFin()         { return dateFin; }
    public String getMotif()              { return motif; }
    public StatutDemande getStatut()      { return statut; }
    public LocalDateTime getDateDemande() { return dateDemande; }
    public UUID getUtilisateurId()        { return utilisateurId; }
    public String getUtilisateurNom()     { return utilisateurNom; }
    public String getUtilisateurPrenom()  { return utilisateurPrenom; }
}
