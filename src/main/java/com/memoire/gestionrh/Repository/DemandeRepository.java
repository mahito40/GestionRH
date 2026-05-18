package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Demande;
import com.memoire.gestionrh.enums.StatutDemande;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
     List<Demande> findByUtilisateurId(Long utilisateurId);

    List<Demande> findByStatut(StatutDemande statut);
}