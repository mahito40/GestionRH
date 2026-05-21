package com.memoire.gestionrh.Config;

import com.memoire.gestionrh.Models.Departement;
import com.memoire.gestionrh.Models.Role;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Models.service;
import com.memoire.gestionrh.Repository.DepartementRepository;
import com.memoire.gestionrh.Repository.RoleRepository;
import com.memoire.gestionrh.Repository.ServiceRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DefaultRhInitializer {

    private final UtilisateursRepository utilisateursRepository;
    private final DepartementRepository departementRepository;
    private final ServiceRepository serviceRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-rh.email:rh@homintec.local}")
    private String defaultRhEmail;

    @Value("${app.default-rh.password:Rh@12345}")
    private String defaultRhPassword;

    @Value("${app.default-rh.nom:RH}")
    private String defaultRhNom;

    @Value("${app.default-rh.prenom:Default}")
    private String defaultRhPrenom;

    @Value("${app.default-rh.departement:Administratif}")
    private String defaultDepartementName;

    @Value("${app.default-rh.service:Ressources Humaines}")
    private String defaultServiceName;

    @Value("${app.default-rh.role:RH}")
    private String defaultRoleName;

    @Bean
    CommandLineRunner seedDefaultRhUser() {
        return args -> {
            Departement departement = departementRepository.findAll().stream()
                    .filter(d -> d.getNom() != null && d.getNom().equalsIgnoreCase(defaultDepartementName))
                    .findFirst()
                    .orElseGet(() -> {
                        Departement dep = new Departement();
                        dep.setNom(defaultDepartementName);
                        dep.setDescription("Departement par defaut pour la gestion RH");
                        return departementRepository.save(dep);
                    });

            service serviceRh = serviceRepository.findAll().stream()
                    .filter(s -> s.getNom() != null && s.getNom().equalsIgnoreCase(defaultServiceName))
                    .findFirst()
                    .orElseGet(() -> {
                        service s = new service();
                        s.setNom(defaultServiceName);
                        s.setDescription("Service RH par defaut");
                        s.setDepartement(departement);
                        return serviceRepository.save(s);
                    });

            if (serviceRh.getDepartement() == null) {
                serviceRh.setDepartement(departement);
                serviceRh = serviceRepository.save(serviceRh);
            }

            Role roleRh = roleRepository.findByNomIgnoreCase(defaultRoleName)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setNom(defaultRoleName.toUpperCase());
                        role.setDescription("Role RH par defaut");
                        return roleRepository.save(role);
                    });

            Utilisateur utilisateur = utilisateursRepository.findByEmail(defaultRhEmail)
                    .orElseGet(Utilisateur::new);

            utilisateur.setNom(defaultRhNom);
            utilisateur.setPrenom(defaultRhPrenom);
            utilisateur.setEmail(defaultRhEmail);
            utilisateur.setPoste("RH");
            utilisateur.setRole(roleRh);
            utilisateur.setService(serviceRh);
            utilisateur.setMotDePasse(passwordEncoder.encode(defaultRhPassword));
            utilisateur.setIsfirstlogin(false);

            utilisateursRepository.save(utilisateur);

            log.warn("Compte RH par defaut cree: email={} motDePasse={}", defaultRhEmail, defaultRhPassword);
        };
    }
}
