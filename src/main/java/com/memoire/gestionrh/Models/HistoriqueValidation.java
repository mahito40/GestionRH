package com.memoire.gestionrh.Models;

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
    private Utilisateur validateur; // MANAGER, RH ou DG

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public StatutValidation getStatut() {
        return statut;
    }

    public void setStatut(StatutValidation statut) {
        this.statut = statut;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Utilisateur getValidateur() {
        return validateur;
    }

    public void setValidateur(Utilisateur validateur) {
        this.validateur = validateur;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
