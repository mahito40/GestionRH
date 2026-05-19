package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.RoleResponseDTO;
import com.memoire.gestionrh.DTO.UtilisateurRequestDTO;
import com.memoire.gestionrh.DTO.UtilisateurResponseDTO;
import com.memoire.gestionrh.Models.Role;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Models.service;
import com.memoire.gestionrh.Repository.RoleRepository;
import com.memoire.gestionrh.Repository.ServiceRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateursRepository utilisateursRepository;
    private final ServiceRepository serviceRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UtilisateurResponseDTO create(UtilisateurRequestDTO dto) {
        if (utilisateursRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe deja.");
        }

        Utilisateur utilisateur = new Utilisateur();
        applyFields(utilisateur, dto);

        String temporaryPassword = generateTempPassword();
        utilisateur.setMotDePasse(passwordEncoder.encode(temporaryPassword));
        utilisateur.setIsfirstlogin(true);

        Utilisateur saved = utilisateursRepository.save(utilisateur);

        emailService.envoyerCodeProvisoire(saved.getEmail(), saved.getNom(), temporaryPassword);

        return toResponse(saved);
    }

    public List<UtilisateurResponseDTO> getAll() {
        return utilisateursRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UtilisateurResponseDTO getById(UUID id) {
        Utilisateur utilisateur = utilisateursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
        return toResponse(utilisateur);
    }

    public UtilisateurResponseDTO update(UUID id, UtilisateurRequestDTO dto) {
        Utilisateur utilisateur = utilisateursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        applyFields(utilisateur, dto);
        Utilisateur saved = utilisateursRepository.save(utilisateur);
        return toResponse(saved);
    }

    public void delete(UUID id) {
        utilisateursRepository.deleteById(id);
    }

    private void applyFields(Utilisateur utilisateur, UtilisateurRequestDTO dto) {
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setPoste(dto.getPoste());

        if (dto.getServiceId() != null) {
            service serviceEntity = serviceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service introuvable."));
            utilisateur.setService(serviceEntity);
        }

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role introuvable."));
            utilisateur.setRole(role);
        }
    }

    private UtilisateurResponseDTO toResponse(Utilisateur utilisateur) {
        RoleResponseDTO role = null;
        if (utilisateur.getRole() != null) {
            role = new RoleResponseDTO(
                    utilisateur.getRole().getId(),
                    utilisateur.getRole().getNom(),
                    utilisateur.getRole().getDescription());
        }

        return new UtilisateurResponseDTO(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getPoste(),
                utilisateur.getService() != null ? utilisateur.getService().getId() : null,
                utilisateur.getIsfirstlogin(),
                role);
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
