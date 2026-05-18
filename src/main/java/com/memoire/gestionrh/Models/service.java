package com.memoire.gestionrh.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "Services")

public class service {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    
    @ManyToOne
    @JoinColumn
    (name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;


     public Long getId() {
        return id;
     }
     public String getNom() {
        return nom;
     }
     public void setNom(String nom) {
        this.nom = nom;
     }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Utilisateur getUtilisateur(){
        return utilisateur;
    }
    public void setUtilisateur( Utilisateur utilisateur){

        this.utilisateur=utilisateur;
    }
    public Departement getDepartement() {
        return departement;
    }
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
    
}

    


