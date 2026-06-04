package com.memoire.gestionrh.DTO;

import com.memoire.gestionrh.enums.StatutConversation;
import java.time.LocalDateTime;
import java.util.UUID;

public class ConversationDTO {

    private UUID id;
    private StatutConversation statut;
    private LocalDateTime dateCreation;
    private LocalDateTime lastMessage;
    private UUID expediteurId;
    private String expediteurNom;
    private UUID destinataireId;
    private String destinataireNom;

    // Constructeurs
    public ConversationDTO() {}

    public ConversationDTO(UUID id, String titre, StatutConversation statut,
                           LocalDateTime dateCreation, LocalDateTime lastMessage,
                           UUID expediteurId, String expediteurNom,
                           UUID destinataireId, String destinataireNom) {
        this.id = id;
        this.statut = statut;
        this.dateCreation = dateCreation;
        this.lastMessage = lastMessage;
        this.expediteurId = expediteurId;
        this.expediteurNom = expediteurNom;
        this.destinataireId = destinataireId;
        this.destinataireNom = destinataireNom;
    }

    // Getters & Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public StatutConversation getStatut() { return statut; }
    public void setStatut(StatutConversation statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getLastMessage() { return lastMessage; }
    public void setLastMessage(LocalDateTime lastMessage) { this.lastMessage = lastMessage; }

    public UUID getexpediteurId() { return expediteurId; }
    public void setexpediteurId(UUID expediteurId) { this.expediteurId = expediteurId; }

    public String getexpediteurNom() { return expediteurNom; }
    public void setexpediteurNom(String expediteurNom) { this.expediteurNom = expediteurNom; }

    public UUID getdestinataireId() { return destinataireId; }
    public void setdestinataireId(UUID destinataireId) { this.destinataireId = destinataireId; }

    public String getdestinataireNom() { return destinataireNom; }
    public void setdestinataireNom(String destinataireNom) { this.destinataireNom = destinataireNom; }
}
