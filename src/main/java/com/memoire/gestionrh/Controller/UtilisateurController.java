package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.UtilisateurRequestDTO;
import com.memoire.gestionrh.DTO.UtilisateurResponseDTO;
import com.memoire.gestionrh.Service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public UtilisateurResponseDTO create(@RequestBody UtilisateurRequestDTO dto) {
        return utilisateurService.create(dto);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<UtilisateurResponseDTO> getAll() {
        return utilisateurService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public UtilisateurResponseDTO getById(@PathVariable UUID id) {
        return utilisateurService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public UtilisateurResponseDTO update(@PathVariable UUID id, @RequestBody UtilisateurRequestDTO dto) {
        return utilisateurService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public void delete(@PathVariable UUID id) {
        utilisateurService.delete(id);
    }
}
