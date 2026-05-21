package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageWebSocketDTO {
    private UUID id;
    private UUID expediteurId;
    private String expediteurNom;
    private UUID destinataireId;
    private String contenu;
    private LocalDateTime dateEnvoi;
}
