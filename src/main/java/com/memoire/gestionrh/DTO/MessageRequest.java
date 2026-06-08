package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.util.UUID;

@Data
public class MessageRequest {
     private String contenu;
    private UUID expediteurId;
    private UUID conversationId;
    
}
