package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.HistoriqueValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueValidationRepository extends JpaRepository<HistoriqueValidation, Long> {
    List<HistoriqueValidation> findByDemandeId(Long demandeId);
}
