package com.memoire.gestionrh.DTO;

import lombok.Data;

@Data
public class ServiceDTO {
    private String nom;
    private String description;

    public ServiceDTO(Long id, String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
    
}
