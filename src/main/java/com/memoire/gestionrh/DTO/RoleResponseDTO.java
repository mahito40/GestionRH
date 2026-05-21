package com.memoire.gestionrh.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RoleResponseDTO {
    private UUID id;
    private String nom;
    private String description;
}
