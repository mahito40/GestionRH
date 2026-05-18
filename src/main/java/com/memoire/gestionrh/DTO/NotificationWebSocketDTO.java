package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationWebSocketDTO {
    private Long id;
    private Long utilisateurId;
    private String type;
    private String contenu;
    private boolean lu;
    private LocalDateTime dateEnvoi;
}
