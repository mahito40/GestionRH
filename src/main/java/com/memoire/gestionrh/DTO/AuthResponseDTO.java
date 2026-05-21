package com.memoire.gestionrh.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String poste;
    private UUID departementId;
    private Boolean premiereCo;
    private RoleResponseDTO role;
}
