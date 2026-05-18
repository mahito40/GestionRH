package com.memoire.gestionrh.Repository;
import com.memoire.gestionrh.Models.service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<service, Long> {
    
}
