package com.memoire.gestionrh.DTO;

import lombok.Data;

@Data
public class NotificationDTO {
    private String contenu;
    private Long utilisateurId;
}
