package com.memoire.gestionrh.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ServiceResponseDTO {
    private UUID id;
    private String nom;
    private String description;
    private UUID departementId;
    private long nbEmployes;
}
