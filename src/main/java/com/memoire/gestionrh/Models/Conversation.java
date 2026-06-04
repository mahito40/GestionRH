package com.memoire.gestionrh.Models;

import com.memoire.gestionrh.enums.StatutConversation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutConversation statut = StatutConversation.active;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "dernier_message")
    private LocalDateTime lastMessage;

    @ManyToOne
    @JoinColumn(name = "expediteur_id", nullable = false)
    private Utilisateur expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Utilisateur destinataire;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Message> messages;

    public UUID getId() { return id; }


    public StatutConversation getStatut() { return statut; }
    public void setStatut(StatutConversation statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }

    public LocalDateTime getLastMessage() { return lastMessage; }
    public void setLastMessage(LocalDateTime lastMessage) { this.lastMessage = lastMessage; }

    public Utilisateur getexpediteur() { return expediteur; }
    public void setexpediteur(Utilisateur expediteur) { this.expediteur = expediteur; }

    public Utilisateur getdestinataire() { return destinataire; }
    public void setdestinataire(Utilisateur destinataire) { this.destinataire = destinataire; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
}  