package com.memoire.gestionrh.Models;

import com.memoire.gestionrh.enums.StatutDemande;
import com.memoire.gestionrh.enums.TypeDemande;

import java.time.LocalDate;
import java.time.LocalDateTime;


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
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToOne(mappedBy = "demande")
    private Justificatif justificatif;
    
    public Long getId() {
        return id;
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

    public StatutDemande getStatut() {
    return statut;
}

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
}

public Justificatif getJustificatif() {
    return justificatif;
}

public void setJustificatif(Justificatif justificatif) {
    this.justificatif = justificatif;
}

}
