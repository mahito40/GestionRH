package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateursRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByPoste(String poste);

    List<Utilisateur> findByService_Departement_Id(Long departementId);

    Optional<Utilisateur> findByService_Departement_IdAndPoste(
            Long departementId,
            String poste
    );
}