package com.memoire.gestionrh.DTO;

import com.memoire.gestionrh.enums.StatutValidation;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ValidationDTO {

    private String nomRemplacant;
private LocalDate debutCollaboration;
private Integer nombreJoursConsommes;
private LocalDate dateDerniersConges;
private Integer nombreJoursDisponibles;

private Boolean deductionDroitConges;
private Boolean deductionPaie;
private Boolean demandeReglementaire;

private String observation;

    private StatutValidation statut; // APPROUVE ou REFUSE
    private UUID validateurId; // id du chef service, RH ou DG
}
