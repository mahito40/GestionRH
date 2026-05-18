package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageWebSocketDTO {
    private Long id;
    private Long expediteurId;
    private String expediteurNom;
    private Long destinataireId;
    private String contenu;
    private LocalDateTime dateEnvoi;
}
