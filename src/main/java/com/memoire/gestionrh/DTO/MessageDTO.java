package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.util.UUID;

@Data
public class MessageDTO {
    private String contenu;
    private UUID utilisateurId;
}
