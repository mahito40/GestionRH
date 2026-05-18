package com.memoire.gestionrh.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "departements")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    
    @ManyToOne
    @JoinColumn
    (name = "service_id")
    private service service;

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
    public service getService(){
        return service;
    }
    public void setService( service service){

        this.service=service    ;
    }
    
}
