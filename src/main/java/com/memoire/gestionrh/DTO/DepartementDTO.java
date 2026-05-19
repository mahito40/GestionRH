package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.util.UUID;

@Data
public class DepartementDTO {
    private String nom;
    private String description;

    public DepartementDTO(UUID id, String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
}
