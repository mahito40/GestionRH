package com.memoire.gestionrh.DTO;

import com.memoire.gestionrh.enums.StatutNotification;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationWebSocketDTO {
    private UUID id;
    private String contenu;
    private LocalDateTime datenotif;
    private StatutNotification statut;
    private UUID utilisateurId;
}