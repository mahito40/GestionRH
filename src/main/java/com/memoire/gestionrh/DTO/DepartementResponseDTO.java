package com.memoire.gestionrh.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartementResponseDTO {
    private Long id;
    private String nom;
    private String description;
}
