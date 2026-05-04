package com.memoire.gestionrh.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceResponseDTO {
    private Long id;
    private String nom;
    private String niveau;
    private int nombreUtilisateurs;
}
