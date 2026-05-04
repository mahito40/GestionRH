package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.ServiceDTO;
import com.memoire.gestionrh.DTO.ServiceResponseDTO;
import com.memoire.gestionrh.Models.Departement;
import com.memoire.gestionrh.Models.ServiceEN;
import com.memoire.gestionrh.Repository.ServiceRepository;
import com.memoire.gestionrh.Repository.DepartementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository repo;
    private final DepartementRepository departementRepository;

    public ServiceEN createService(ServiceDTO dto) {

    Departement dep = departementRepository.findById(dto.getDepartementId())
            .orElseThrow(() -> new RuntimeException("Département introuvable"));

    ServiceEN service = new ServiceEN();
    service.setNom(dto.getNom());
    service.setDepartement(dep);

    return repo.save(service);
}
    

    public List<ServiceResponseDTO> getTousLesServicesAvecNombre() {
    return repo.findAll()
            .stream()
            .map(service -> new ServiceResponseDTO(
                service.getId(),
                service.getNom(),
                service.getNiveau(),
                service.getUtilisateurs().size()
            ))
            .collect(Collectors.toList());
}   

    public ServiceEN getServiceParId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service non trouvé avec l'id : " + id));
    }

    public ServiceEN modifierService(@PathVariable Long id,  @RequestBody ServiceDTO dto) {

        ServiceEN service = repo.findById(id)
        .orElseThrow(() -> new RuntimeException("Service non trouvé avec l'id : " + id));

        Departement dep = departementRepository.findById(dto.getDepartementId())
                .orElseThrow(() -> new RuntimeException("Département introuvable"));

        service.setNom(dto.getNom());
        service.setDepartement(dep);

        return repo.save(service);
    }
    public ServiceEN patchService(Long id, ServiceDTO dto) {

        ServiceEN service = repo.findById(id)
        .orElseThrow(() -> new RuntimeException("Service non trouvé avec l'id : " + id));

        if (dto.getNom() != null) {
            service.setNom(dto.getNom());
        }

        if (dto.getDepartementId() != null) {
            Departement dep = departementRepository.findById(dto.getDepartementId())
                    .orElseThrow(() -> new RuntimeException("Département introuvable"));
            service.setDepartement(dep);
        }

        return repo.save(service);
    }

    public void supprimerService(Long id) {
        getServiceParId(id);
        repo.deleteById(id);
    }
}