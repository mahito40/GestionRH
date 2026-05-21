package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Demande;
import com.memoire.gestionrh.enums.StatutDemande;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<Demande, UUID> {
    List<Demande> findByUtilisateurId(UUID utilisateurId);

    List<Demande> findByStatut(StatutDemande statut);
}