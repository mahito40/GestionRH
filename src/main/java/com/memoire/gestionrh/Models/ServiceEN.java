package com.memoire.gestionrh.Models;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "services")
public class ServiceEN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String niveau;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    @JsonIgnore
    private Departement departement;

    @OneToMany(mappedBy = "service")
    private List<Utilisateur> utilisateurs;

     public Long getId() {
        return id;
     }
     public String getNom() {
        return nom;
     }
     public void setNom(String nom) {
        this.nom = nom;
     }
    public String getNiveau() {
        return niveau;
    }
    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    public Departement getDepartement() {
        return departement ;
    }
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }
    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
}
}