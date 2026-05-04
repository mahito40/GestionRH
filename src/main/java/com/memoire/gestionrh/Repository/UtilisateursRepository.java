package com.memoire.gestionrh.Repository;
import com.memoire.gestionrh.Models.Utilisateur;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UtilisateursRepository  extends JpaRepository<Utilisateur, Long> {

     Optional<Utilisateur> findByEmail(String email);
}
 