package com.memoire.gestionrh.Models;

import jakarta.persistence.*;
import java.util.List;


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
    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEN service;

     @OneToMany(mappedBy = "utilisateur")
    private List<Message> messages;

    @OneToMany(mappedBy = "utilisateur")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "utilisateur")
    private List<Demande> demandes;

    private String motDePasse;

   private boolean doitChangerMotDePasse = true; // true par défaut à la création

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
       public void setService(ServiceEN service) {
         this.service = service;
       }
      public ServiceEN getService() {
         return service;
      }
      public List<Message> getMessages() {
         return messages;
      }
      public void setMessages(List<Message> messages) {
         this.messages = messages;
      }
      public List<Notification> getNotifications() {
         return notifications;
      }
      public void setNotifications(List<Notification> notifications) {
         this.notifications = notifications;
      }
      public List<Demande> getDemandes() {
         return demandes;
      }
      public void setDemandes(List<Demande> demandes) {
         this.demandes = demandes;
      }
      public String getMotDePasse() {
         return motDePasse;
      }
      public void setMotDePasse(String motDePasse) {
         this.motDePasse = motDePasse;
      }
      public boolean getDoitChangerMotDePasse() {
         return doitChangerMotDePasse;
      }
      public void setDoitChangerMotDePasse(boolean doitChangerMotDePasse) {
         this.doitChangerMotDePasse = doitChangerMotDePasse;
      }  
}
