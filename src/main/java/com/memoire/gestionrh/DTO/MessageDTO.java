package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDTO {
    // Pour l'envoi (input)
    private String contenu;
    private UUID expediteurId;
    private UUID conversationId;

    // Pour la réception (output WebSocket)
    private UUID id;
    private LocalDateTime dateEnvoi;
    private String expediteurNom;
    private boolean lu;
}
