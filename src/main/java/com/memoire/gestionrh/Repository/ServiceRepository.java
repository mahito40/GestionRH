package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<service, UUID> {
    boolean existsByNomIgnoreCase(String nom);

    List<service> findByDepartement_Id(UUID departementId);

    long countByDepartement_Id(UUID departementId);
}
