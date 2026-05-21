package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.util.UUID;

@Data
public class ServiceRequestDTO {
    private String nom;
    private String description;
    private UUID departementId;
}
