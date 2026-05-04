package com.memoire.gestionrh.DTO;

import lombok.Data;

@Data
public class RegisterDTO {
    private String nom;
    private String prenom;
    private String email;
    private String poste;
    private Long serviceId;
}
