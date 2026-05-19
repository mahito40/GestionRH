package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.ServiceRequestDTO;
import com.memoire.gestionrh.DTO.ServiceResponseDTO;
import com.memoire.gestionrh.Service.ServiceRHService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceRHController {

    private final ServiceRHService serviceRHService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public ServiceResponseDTO create(@RequestBody ServiceRequestDTO dto) {
        return serviceRHService.create(dto);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<ServiceResponseDTO> getAll() {
        return serviceRHService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ServiceResponseDTO getById(@PathVariable UUID id) {
        return serviceRHService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public ServiceResponseDTO update(@PathVariable UUID id, @RequestBody ServiceRequestDTO dto) {
        return serviceRHService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RH')")
    public void delete(@PathVariable UUID id) {
        serviceRHService.delete(id);
    }
}
