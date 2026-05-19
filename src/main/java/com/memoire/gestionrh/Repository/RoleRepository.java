package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCase(String nom);
}
