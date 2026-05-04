package com.memoire.gestionrh.Models;

import java.time.LocalDateTime;

import com.memoire.gestionrh.enums.StatutNotification;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "date_notification")
    private LocalDateTime datenotif;

    @Enumerated(EnumType.STRING)
    private StatutNotification statut = StatutNotification.NON_LU;

    @ManyToOne
@JoinColumn(name = "utilisateur_id")
private Utilisateur utilisateur;

    
    public Long getId() {
        return id;
    }
    public String getContenu() {
        return contenu;
    }
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    public LocalDateTime getDatenotif() {
        return datenotif;
    }
    public void setDatenotif(LocalDateTime datenotif) {
        this.datenotif = datenotif;
    }
    public StatutNotification getStatut() {
        return statut;
    }
    public void setStatut(StatutNotification statut) {
        this.statut = statut;
    }
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
