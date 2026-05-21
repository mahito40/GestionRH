package com.memoire.gestionrh.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DepartementResponseDTO {
    private UUID id;
    private String nom;
    private String description;
    private long nbServices;
}
