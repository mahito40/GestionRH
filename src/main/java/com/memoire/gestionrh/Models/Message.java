package com.memoire.gestionrh.Models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Utilisateur sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Utilisateur receiver;

    public UUID getId() {
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

    public Utilisateur getSender() {
        return sender;
    }

    public void setSender(Utilisateur sender) {
        this.sender = sender;
    }

    public Utilisateur getReceiver() {
        return receiver;
    }

    public void setReceiver(Utilisateur receiver) {
        this.receiver = receiver;
    }
}
