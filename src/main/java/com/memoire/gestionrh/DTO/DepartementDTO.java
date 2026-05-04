package com.memoire.gestionrh.DTO;

import lombok.Data;


@Data
public class DepartementDTO {
    private Long id;
    private String nom;
    private String description;
    private int nombreServices;

    public DepartementDTO(Long id, String nom, String description, int nombreServices) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.nombreServices = nombreServices;
    }
}
