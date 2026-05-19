package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.util.UUID;

@Data
public class UtilisateurRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private String poste;
    private UUID serviceId;
    private UUID roleId;
}
