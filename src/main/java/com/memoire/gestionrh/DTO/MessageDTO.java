package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDTO {
    // Pour la réception (output WebSocket)
   private UUID id;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private UUID expediteurId;
    private String expediteurNom;
    private UUID conversationId;
    private boolean lu;
}
