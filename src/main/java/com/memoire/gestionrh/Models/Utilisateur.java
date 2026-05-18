package com.memoire.gestionrh.Models;

import jakarta.persistence.*;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;


@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private String poste;

    
    @CreationTimestamp
    @Column(updatable = false)
     private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

   @ManyToOne
    @JoinColumn(name = "service_id")
    private service service;

    @ManyToOne
    @JoinColumn(name = "superieur_id")
    private Utilisateur superieur;
    
    @OneToMany(mappedBy = "superieur")
    private List<Utilisateur> subordinates;

    @ManyToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;
    
    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;

    private String motDePasse;
    @Column(name = "isfirstlogin")
    private boolean isfirstlogin = true; // true par défaut à la création

     public Long getId() {
        return id;
     }
     public String getNom() {
        return nom;
     }
     public void setNom(String nom) {
        this.nom = nom;
     }
     public String getPrenom() {
        return prenom;
     }
     public void setPrenom(String prenom) {
        this.prenom = prenom;
     }
     public String getEmail() {
        return email;
     }
     public void setEmail(String email) {
        this.email = email;
     }
     public String getPoste() {
        return poste;
     }
     public void setPoste(String poste) {
        this.poste = poste;
     }
      public service getService() {
         return service;
      }
      public void setService(service service) {
         this.service = service;
      }
      public Utilisateur getSuperieur() {
         return superieur;
      }
      public void setSuperieur(Utilisateur superieur) {
         this.superieur = superieur;
      }
      public String getMotDePasse() {
         return motDePasse;
      }
      public void setMotDePasse(String motDePasse) {
         this.motDePasse = motDePasse;
      }
      public boolean getIsfirstlogin() {
         return isfirstlogin;
      }
      public void setIsfirstlogin(boolean isfirstlogin) {
         this.isfirstlogin = isfirstlogin;
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
      public Demande getDemande() {
         return demande;
      }
      public void setDemande(Demande demande) {
         this.demande = demande;
      }
}
