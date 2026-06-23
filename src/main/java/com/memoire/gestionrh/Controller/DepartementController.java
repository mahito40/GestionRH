package com.memoire.gestionrh.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.memoire.gestionrh.DTO.DepartementDTO;
import com.memoire.gestionrh.DTO.DepartementResponseDTO;
import com.memoire.gestionrh.Models.Departement;
import com.memoire.gestionrh.Service.DepartementService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departements")
@RequiredArgsConstructor
public class DepartementController {

   
    private final DepartementService departementService;

    @PostMapping
    @PreAuthorize("hasAnyRole('RH','ADMIN')")
    public Departement createDepartement(@RequestBody DepartementDTO dto) {
        return departementService.createDepartement(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RH','ADMIN')")
    public ResponseEntity<List<DepartementResponseDTO>> getTousLesDepartements() {
        return ResponseEntity.ok(departementService.getTousLesDepartements());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH','ADMIN')")
    public ResponseEntity<Departement> getDepartementById(@PathVariable UUID id) {
        return ResponseEntity.ok(departementService.getDepartementParId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH','ADMIN')")
    public Departement updateDepartement(@PathVariable UUID id,
            @RequestBody DepartementDTO dto) {
        return departementService.modifierDepartement(id, dto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH','ADMIN')")
    public void patchDepartement(@PathVariable UUID id,
            @RequestBody DepartementDTO dto) {
        departementService.patchDepartement(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH','ADMIN')")
    public void supprimerDepartement(@PathVariable UUID id) {
        departementService.supprimerDepartement(id);
    }

}
