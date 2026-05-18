package com.memoire.gestionrh.Service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.memoire.gestionrh.DTO.DepartementDTO;
import com.memoire.gestionrh.DTO.DepartementResponseDTO;
import com.memoire.gestionrh.Models.Departement;
import com.memoire.gestionrh.Repository.DepartementRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartementService {

    private final DepartementRepository departementRepository;

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
                    dep.getDescription()
                ))
                .collect(Collectors.toList());
    }
    public Departement getDepartementParId(Long id) {
        return departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'id : " + id));
    }
    public Departement modifierDepartement(Long id, DepartementDTO dto) {
        Departement dep = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'id : " + id));

        dep.setNom(dto.getNom());
        dep.setDescription(dto.getDescription());

        return departementRepository.save(dep);
    }
    public void patchDepartement(Long id, DepartementDTO dto) {
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
    public void supprimerDepartement(Long id) {
        departementRepository.deleteById(id);
    }

}
