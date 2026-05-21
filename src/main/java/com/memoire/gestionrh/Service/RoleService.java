package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.RoleDTO;
import com.memoire.gestionrh.DTO.RoleResponseDTO;
import com.memoire.gestionrh.Models.Role;
import com.memoire.gestionrh.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleResponseDTO create(RoleDTO dto) {
        if (roleRepository.existsByNomIgnoreCase(dto.getNom())) {
            throw new RuntimeException("Ce role existe deja.");
        }

        Role role = new Role();
        role.setNom(dto.getNom().trim().toUpperCase());
        role.setDescription(dto.getDescription());

        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    public List<RoleResponseDTO> getAll() {
        return roleRepository.findAll().stream().map(this::toResponse).toList();
    }

    public RoleResponseDTO getById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role introuvable."));
        return toResponse(role);
    }

    public RoleResponseDTO update(UUID id, RoleDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role introuvable."));

        if (dto.getNom() != null && !dto.getNom().isBlank()) {
            role.setNom(dto.getNom().trim().toUpperCase());
        }
        role.setDescription(dto.getDescription());

        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }

    private RoleResponseDTO toResponse(Role role) {
        return new RoleResponseDTO(role.getId(), role.getNom(), role.getDescription());
    }
}
