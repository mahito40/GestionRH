package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationWebSocketDTO {
    private UUID id;
    private UUID utilisateurId;
    private String type;
    private String contenu;
    private boolean lu;
    private LocalDateTime dateEnvoi;
}
