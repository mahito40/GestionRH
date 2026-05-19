package com.memoire.gestionrh.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UtilisateurResponseDTO {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String poste;
    private UUID serviceId;
    private Boolean premiereCo;
    private RoleResponseDTO role;
}
