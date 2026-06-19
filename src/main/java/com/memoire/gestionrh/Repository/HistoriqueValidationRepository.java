package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.HistoriqueValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;


public interface HistoriqueValidationRepository extends JpaRepository<HistoriqueValidation, UUID> {
    List<HistoriqueValidation> findByDemandeId(UUID demandeId);
}
