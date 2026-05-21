package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.ServiceRequestDTO;
import com.memoire.gestionrh.DTO.ServiceResponseDTO;
import com.memoire.gestionrh.Models.Departement;
import com.memoire.gestionrh.Models.service;
import com.memoire.gestionrh.Repository.DepartementRepository;
import com.memoire.gestionrh.Repository.ServiceRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceRHService {

    private final ServiceRepository serviceRepository;
    private final DepartementRepository departementRepository;
    private final UtilisateursRepository utilisateursRepository;

    public ServiceResponseDTO create(ServiceRequestDTO dto) {
        if (serviceRepository.existsByNomIgnoreCase(dto.getNom())) {
            throw new RuntimeException("Ce service existe deja.");
        }

        service entity = new service();
        entity.setNom(dto.getNom().trim());
        entity.setDescription(dto.getDescription());
        applyRelations(entity, dto);

        service saved = serviceRepository.save(entity);
        return toResponse(saved);
    }

    public List<ServiceResponseDTO> getAll() {
        return serviceRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ServiceResponseDTO getById(UUID id) {
        service entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service introuvable."));
        return toResponse(entity);
    }

    public ServiceResponseDTO update(UUID id, ServiceRequestDTO dto) {
        service entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service introuvable."));

        if (dto.getNom() != null && !dto.getNom().isBlank()) {
            entity.setNom(dto.getNom().trim());
        }
        entity.setDescription(dto.getDescription());
        applyRelations(entity, dto);

        service saved = serviceRepository.save(entity);
        return toResponse(saved);
    }

    public void delete(UUID id) {
        serviceRepository.deleteById(id);
    }

    private void applyRelations(service entity, ServiceRequestDTO dto) {
        if (dto.getDepartementId() != null) {
            Departement departement = departementRepository.findById(dto.getDepartementId())
                    .orElseThrow(() -> new RuntimeException("Departement introuvable."));
            entity.setDepartement(departement);
        }
    }

    private ServiceResponseDTO toResponse(service entity) {
        UUID departementId = entity.getDepartement() != null ? entity.getDepartement().getId() : null;
        long nbEmployes = utilisateursRepository.countByService_Id(entity.getId());

        return new ServiceResponseDTO(
                entity.getId(),
                entity.getNom(),
                entity.getDescription(),
                departementId,
                nbEmployes);
    }
}
