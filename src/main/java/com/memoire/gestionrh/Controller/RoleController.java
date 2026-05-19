package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.RoleDTO;
import com.memoire.gestionrh.DTO.RoleResponseDTO;
import com.memoire.gestionrh.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public RoleResponseDTO create(@RequestBody RoleDTO dto) {
        return roleService.create(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public List<RoleResponseDTO> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public RoleResponseDTO getById(@PathVariable UUID id) {
        return roleService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public RoleResponseDTO update(@PathVariable UUID id, @RequestBody RoleDTO dto) {
        return roleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public void delete(@PathVariable UUID id) {
        roleService.delete(id);
    }
}
