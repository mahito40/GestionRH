package com.memoire.gestionrh.DTO;

import com.memoire.gestionrh.enums.StatutValidation;
import lombok.Data;
import java.util.UUID;

@Data
public class ValidationDTO {
    private StatutValidation statut; // APPROUVE ou REFUSE
    private UUID validateurId; // id du chef service, RH ou DG
}
