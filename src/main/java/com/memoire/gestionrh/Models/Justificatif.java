package com.memoire.gestionrh.Models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "justificatifs")
public class Justificatif {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String path;

    private String description;

    @OneToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;

    public UUID getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }
}