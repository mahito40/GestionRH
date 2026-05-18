package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.ValidationDTO;
import com.memoire.gestionrh.Models.*;
import com.memoire.gestionrh.Repository.HistoriqueValidationRepository;
import com.memoire.gestionrh.Repository.DemandeRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoriqueValidationService {

    private final HistoriqueValidationRepository validationRepository;
    private final DemandeRepository demandeRepository;
    private final UtilisateursRepository utilisateurRepository;

    public HistoriqueValidation validerDemande(Long demandeId, ValidationDTO dto) {

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        Utilisateur validateur = utilisateurRepository.findById(dto.getValidateurId())
                .orElseThrow(() -> new RuntimeException("Validateur introuvable"));

        HistoriqueValidation validation = new HistoriqueValidation();
        validation.setDemande(demande);
        validation.setValidateur(validateur);
        validation.setStatut(dto.getStatut());
        validation.setDateValidation(LocalDateTime.now());

        return validationRepository.save(validation);
    }

    public List<HistoriqueValidation> getValidationsByDemande(Long demandeId) {
        return validationRepository.findByDemandeId(demandeId);
    }
}
