package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Justificatif;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;


public interface JustificatifRepository extends JpaRepository<Justificatif, UUID> {

    Optional<Justificatif> findByDemandeId(UUID demandeId);

}
