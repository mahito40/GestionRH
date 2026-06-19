package com.memoire.gestionrh.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.memoire.gestionrh.enums.StatutValidation;

import jakarta.persistence.*;

@Entity
@Table(name = "validations")
public class HistoriqueValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutValidation statut = StatutValidation.EN_ATTENTE;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @ManyToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "validateur_id")
    private Utilisateur validateur;

    // ── Informations RH ──────────────────────────────────────

    @Column(name = "nom_remplacant")
    private String nomRemplacant;

    @Column(name = "absence_deduire_conges")
    private Boolean absenceDeduireConges = false;

    @Column(name = "absence_deduire_paie")
    private Boolean absenceDeduirePaie = false;

    @Column(name = "demande_reglementaire")
    private Boolean demandeReglementaire = false;

    @Column(name = "debut_collaboration")
    private LocalDate debutCollaboration;

    @Column(name = "nombre_jours_consommes")
    private Integer nombreJoursConsommes;

    @Column(name = "date_derniers_conges")
    private LocalDate dateDerniersConges;

    @Column(name = "nombre_jours_disponibles")
    private Integer nombreJoursDisponibles;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    // ── Timestamps ───────────────────────────────────────────

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ── Getters & Setters ────────────────────────────────────

    public UUID getId() { return id; }

    public StatutValidation getStatut() { return statut; }
    public void setStatut(StatutValidation statut) { this.statut = statut; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }

    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }

    public Utilisateur getValidateur() { return validateur; }
    public void setValidateur(Utilisateur validateur) { this.validateur = validateur; }

    public String getNomRemplacant() { return nomRemplacant; }
    public void setNomRemplacant(String nomRemplacant) { this.nomRemplacant = nomRemplacant; }

    public Boolean isAbsenceDeduireConges() { return absenceDeduireConges; }
    public void setAbsenceDeduireConges(Boolean absenceDeduireConges) { this.absenceDeduireConges = absenceDeduireConges; }

    public Boolean isAbsenceDeduirePaie() { return absenceDeduirePaie; }
    public void setAbsenceDeduirePaie(Boolean absenceDeduirePaie) { this.absenceDeduirePaie = absenceDeduirePaie; }

    public Boolean isDemandeReglementaire() { return demandeReglementaire; }
    public void setDemandeReglementaire(Boolean demandeReglementaire) { this.demandeReglementaire = demandeReglementaire; }

    public LocalDate getDebutCollaboration() { return debutCollaboration; }
    public void setDebutCollaboration(LocalDate debutCollaboration) { this.debutCollaboration = debutCollaboration; }

    public Integer getNombreJoursConsommes() { return nombreJoursConsommes; }
    public void setNombreJoursConsommes(Integer nombreJoursConsommes) { this.nombreJoursConsommes = nombreJoursConsommes; }

    public LocalDate getDateDerniersConges() { return dateDerniersConges; }
    public void setDateDerniersConges(LocalDate dateDerniersConges) { this.dateDerniersConges = dateDerniersConges; }

    public Integer getNombreJoursDisponibles() { return nombreJoursDisponibles; }
    public void setNombreJoursDisponibles(Integer nombreJoursDisponibles) { this.nombreJoursDisponibles = nombreJoursDisponibles; }

    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}