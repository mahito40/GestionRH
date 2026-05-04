package com.memoire.gestionrh.Models;

import java.time.LocalDateTime;

import com.memoire.gestionrh.enums.StatutValidation;

import jakarta.persistence.*;


@Entity
@Table(name = "validations")
public class Validation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    public Long getId() {
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
}
