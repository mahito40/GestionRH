package com.memoire.gestionrh.DTO;

import lombok.Data;


@Data
public class DepartementDTO {
    private String nom;
    private String description;

    public DepartementDTO(Long id, String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
}
