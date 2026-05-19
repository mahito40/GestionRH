package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DepartementRepository extends JpaRepository<Departement, UUID> {

}
