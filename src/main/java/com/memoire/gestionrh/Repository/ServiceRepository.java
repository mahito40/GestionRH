package com.memoire.gestionrh.Repository;
import com.memoire.gestionrh.Models.ServiceEN;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEN, Long> {
    
} 
