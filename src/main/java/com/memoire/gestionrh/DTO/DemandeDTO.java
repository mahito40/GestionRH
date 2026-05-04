package com.memoire.gestionrh.DTO;

import java.time.LocalDate;
import lombok.Data;

import com.memoire.gestionrh.enums.TypeDemande;

@Data
public class DemandeDTO {

    private TypeDemande typeDemande;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private Long utilisateurId;
    
}
