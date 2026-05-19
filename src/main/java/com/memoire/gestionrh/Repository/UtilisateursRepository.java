package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UtilisateursRepository extends JpaRepository<Utilisateur, UUID> {

    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByPoste(String poste);

    List<Utilisateur> findByService_Departement_Id(UUID departementId);

    Optional<Utilisateur> findByService_Departement_IdAndPoste(
            UUID departementId,
            String poste);

    long countByService_Id(UUID serviceId);

    long countByService_Departement_Id(UUID departementId);
}