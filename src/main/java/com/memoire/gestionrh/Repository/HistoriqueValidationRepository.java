package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.HistoriqueValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface HistoriqueValidationRepository extends JpaRepository<HistoriqueValidation, UUID> {
    List<HistoriqueValidation> findByDemandeId(UUID demandeId);
}
