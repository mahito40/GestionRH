package com.memoire.gestionrh.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.memoire.gestionrh.DTO.DepartementDTO;
import com.memoire.gestionrh.DTO.DepartementResponseDTO;
import com.memoire.gestionrh.Models.Departement;
import com.memoire.gestionrh.Service.DepartementService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/departements")
public class DepartementController {

    @Autowired
    private DepartementService departementService;

    @PostMapping
    @PreAuthorize("hasRole('RH')")
    public Departement createDepartement(@RequestBody DepartementDTO dto) {
        return departementService.createDepartement(dto);
    }
    @GetMapping
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<List<DepartementResponseDTO>> getTousLesDepartements() {
    return ResponseEntity.ok(departementService.getTousLesDepartements());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<Departement> getDepartementById(@PathVariable Long id) {
        return ResponseEntity.ok(departementService.getDepartementParId(id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RH')")
    public Departement updateDepartement(@PathVariable Long id,
                                       @RequestBody DepartementDTO dto) {
        return departementService.modifierDepartement(id, dto);
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('RH')")
    public void patchDepartement(@PathVariable Long id,
                                @RequestBody DepartementDTO dto) {
        departementService.patchDepartement(id, dto);
    }      
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RH')")  
    public void supprimerDepartement(@PathVariable Long id) {
        departementService.supprimerDepartement(id);
    }

}
