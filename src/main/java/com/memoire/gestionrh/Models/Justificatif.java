package com.memoire.gestionrh.Models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "justificatif")
public class Justificatif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;

    @Column(name = "date_validation")
    private LocalDateTime dateEnvoi;

    @ManyToOne
     @JoinColumn(name = "demande_id")
     private Demande demande;


   public Long getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
    public Demande getDemande() {
        return demande;
    }
    public void setDemande(Demande demande) {
        this.demande = demande;
    }
}
