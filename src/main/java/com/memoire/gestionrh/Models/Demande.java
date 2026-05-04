package com.memoire.gestionrh.Models;

import com.memoire.gestionrh.enums.StatutDemande;
import com.memoire.gestionrh.enums.TypeDemande;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import jakarta.persistence.*;

@Entity
@Table(name = "demandes")
public class Demande {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Enumerated(EnumType.STRING)
    private TypeDemande typeDemande;

    @Column(name = "datedebut")
    private LocalDate dateDebut;

     @Column(name = "datefin")
    private LocalDate dateFin;

    private String motif;

    @Column(name = "datedemande")
    private LocalDateTime dateDemande;

    @Enumerated(EnumType.STRING) 
    private StatutDemande statut = StatutDemande.EN_ATTENTE;

    @ManyToOne
    @JoinColumn(name = "utilidisateur_id")
    private Utilisateur utilisateur;
    
     @OneToMany(mappedBy = "demande")
    private List<Validation> validations;
    
      @OneToMany(mappedBy = "demande")
    private List<Justificatif> justificatifs;

    public List<Validation> getValidations() {
        return validations;
    }
     
    public void setValidations(List<Validation> validations) {
        this.validations = validations;
    }

    public List<Justificatif> getJustificatifs() {
        return justificatifs;
    }
    public void setJustificatifs(List<Justificatif> justificatifs) {
        this.justificatifs = justificatifs;
    }
    public Long getId() {
        return id;
     }
    public StatutDemande getStatut() {
        return statut;
    }
    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }
    public TypeDemande getTypeDemande() {
        return typeDemande;
    }
    public void setTypeDemande(TypeDemande typeDemande) {
        this.typeDemande = typeDemande;
    }   
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    public LocalDate getDateFin() {
        return dateFin;
    }   
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    public String getMotif() {
        return motif;
    }
    public void setMotif(String motif) {
        this.motif = motif;
    }
    public LocalDateTime getDateDemande() {
        return dateDemande;
    }
    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

}
