package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.ServiceDTO;
import com.memoire.gestionrh.DTO.ServiceResponseDTO;
import com.memoire.gestionrh.Models.ServiceEN;
import com.memoire.gestionrh.Service.ServiceService;
import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Services")
public class ServiceController {
    
     @Autowired
    private ServiceService serviceService;
    
    @PostMapping
    @PreAuthorize("hasRole('RH')")
public ServiceEN createService(@RequestBody ServiceDTO dto) {
    return serviceService.createService(dto);
}

   @GetMapping
   @PreAuthorize("hasRole('RH')")
public List<ServiceResponseDTO> getTousLesServices() {
    return serviceService.getTousLesServicesAvecNombre();
}   

     @GetMapping("/{id}")
    @PreAuthorize("hasRole('RH')")  
    public ServiceEN getServiceParId(@PathVariable Long id) {
        return serviceService.getServiceParId(id);
    }

     @PutMapping("/{id}")
    @PreAuthorize("hasRole('RH')")
    public ServiceEN update(@PathVariable Long id,
                            @RequestBody ServiceDTO dto) {
        return serviceService.modifierService(id, dto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('RH')")
    public ServiceEN patch(@PathVariable Long id,
                           @RequestBody ServiceDTO dto) {
        return serviceService.patchService(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RH')")
    public void supprimerService(@PathVariable Long id) {
        serviceService.supprimerService(id);
    }
}
