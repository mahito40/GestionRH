package com.memoire.gestionrh.Controller;

import org.springframework.web.bind.annotation.*;

import com.memoire.gestionrh.DTO.ValidationDTO;
import com.memoire.gestionrh.Service.HistoriqueValidationService;
import com.memoire.gestionrh.Models.HistoriqueValidation;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/validations")
@RequiredArgsConstructor

public class HistoriqueValidationController {

    private final HistoriqueValidationService validationService;

    @PostMapping("/{demandeId}")
    public HistoriqueValidation valider(
            @PathVariable UUID demandeId,
            @RequestBody ValidationDTO dto) {
        return validationService.validerDemande(demandeId, dto);
    }

    @GetMapping("/{demandeId}")
    public List<HistoriqueValidation> getByDemande(@PathVariable UUID demandeId) {
        return validationService.getValidationsByDemande(demandeId);
    }
}
