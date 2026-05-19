package com.memoire.gestionrh.Service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.memoire.gestionrh.DTO.DepartementDTO;
import com.memoire.gestionrh.DTO.DepartementResponseDTO;
import com.memoire.gestionrh.Models.Departement;
import com.memoire.gestionrh.Repository.DepartementRepository;
import com.memoire.gestionrh.Repository.ServiceRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartementService {

    private final DepartementRepository departementRepository;
    private final ServiceRepository serviceRepository;

    public Departement createDepartement(DepartementDTO dto) {
        Departement dep = new Departement();
        dep.setNom(dto.getNom());
        dep.setDescription(dto.getDescription());
        return departementRepository.save(dep);
    }

    public List<DepartementResponseDTO> getTousLesDepartements() {
        return departementRepository.findAll()
                .stream()
                .map(dep -> new DepartementResponseDTO(
                        dep.getId(),
                        dep.getNom(),
                        dep.getDescription(),
                        serviceRepository.countByDepartement_Id(dep.getId())))
                .collect(Collectors.toList());
    }

    public Departement getDepartementParId(UUID id) {
        return departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'id : " + id));
    }

    public Departement modifierDepartement(UUID id, DepartementDTO dto) {
        Departement dep = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'id : " + id));

        dep.setNom(dto.getNom());
        dep.setDescription(dto.getDescription());

        return departementRepository.save(dep);
    }

    public void patchDepartement(UUID id, DepartementDTO dto) {
        Departement dep = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'id : " + id));

        if (dto.getNom() != null) {
            dep.setNom(dto.getNom());
        }
        if (dto.getDescription() != null) {
            dep.setDescription(dto.getDescription());
        }
        departementRepository.save(dep);
    }

    public void supprimerDepartement(UUID id) {
        departementRepository.deleteById(id);
    }

}
