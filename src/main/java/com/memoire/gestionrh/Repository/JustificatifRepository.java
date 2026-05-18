package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Justificatif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JustificatifRepository extends JpaRepository<Justificatif, Long> {

    Optional<Justificatif> findByDemandeId(Long demandeId);

}
