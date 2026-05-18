package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.JustificatifDTO;
import com.memoire.gestionrh.Models.Demande;
import com.memoire.gestionrh.Models.Justificatif;
import com.memoire.gestionrh.Repository.DemandeRepository;
import com.memoire.gestionrh.Repository.JustificatifRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JustificatifService {

    private final JustificatifRepository justificatifRepository;
    private final DemandeRepository demandeRepository;

    public JustificatifService(
            JustificatifRepository justificatifRepository,
            DemandeRepository demandeRepository
    ) {
        this.justificatifRepository = justificatifRepository;
        this.demandeRepository = demandeRepository;
    }

    public JustificatifDTO ajouter(JustificatifDTO dto) {

        Demande demande = demandeRepository.findById(dto.getDemandeId())
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        Justificatif justificatif = new Justificatif();

        justificatif.setPath(dto.getPath());
        justificatif.setDescription(dto.getDescription());
        justificatif.setDemande(demande);

        Justificatif saved = justificatifRepository.save(justificatif);

        return mapToDTO(saved);
    }

    public List<JustificatifDTO> afficherTous() {
        return justificatifRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public JustificatifDTO afficherParId(Long id) {

        Justificatif justificatif = justificatifRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Justificatif introuvable"));

        return mapToDTO(justificatif);
    }

    public void supprimer(Long id) {
        justificatifRepository.deleteById(id);
    }

    private JustificatifDTO mapToDTO(Justificatif justificatif) {

        JustificatifDTO dto = new JustificatifDTO();

        dto.setId(justificatif.getId());
        dto.setPath(justificatif.getPath());
        dto.setDescription(justificatif.getDescription());

        if (justificatif.getDemande() != null) {
            dto.setDemandeId(justificatif.getDemande().getId());
        }

        return dto;
    }
}
