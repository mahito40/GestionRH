package com.memoire.gestionrh.Models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "departements")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    @OneToMany(mappedBy = "departement")
    private List<ServiceEN> services;

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
    
    public void setServices(List<ServiceEN> services) {
        this.services = services;
    }
    public List<ServiceEN> getServices() {
        return services;
    }
    
}
