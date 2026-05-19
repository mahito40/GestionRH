package com.memoire.gestionrh.Models;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;

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
   @JoinColumn(name = "role_id")
   private Role role;

   @OneToMany(mappedBy = "utilisateur")
   @JsonIgnore
   private List<Demande> demandes;

   @JsonIgnore
   @OneToMany(mappedBy = "sender")
   private List<Message> sentMessages;

   @JsonIgnore
   @OneToMany(mappedBy = "receiver")
   private List<Message> receivedMessages;

   private String motDePasse;
   @Column(name = "isfirstlogin")
   private boolean isfirstlogin = true;

   public UUID getId() {
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

   public Role getRole() {
      return role;
   }

   public void setRole(Role role) {
      this.role = role;
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

   public List<Demande> getDemandes() {
      return demandes;
   }

   public void setDemandes(List<Demande> demandes) {
      this.demandes = demandes;
   }
}
