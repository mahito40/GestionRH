package com.memoire.gestionrh.DTO;

import com.memoire.gestionrh.enums.StatutValidation;
import lombok.Data;

@Data
public class ValidationDTO {
    private StatutValidation statut; // APPROUVE ou REFUSE
    private Long validateurId;       // id du chef service, RH ou DG
}
