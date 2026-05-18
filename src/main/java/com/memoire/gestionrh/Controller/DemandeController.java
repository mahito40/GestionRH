package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.DemandeDTO;
import com.memoire.gestionrh.DTO.ValidationDTO;
import com.memoire.gestionrh.Models.Demande;
import com.memoire.gestionrh.Service.DemandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;

    // ── Employé soumet une demande ──
    @PostMapping
    public ResponseEntity<Demande> soumettreDemande(@RequestBody DemandeDTO dto) {
        return ResponseEntity.ok(demandeService.soumettredemande(dto));
    }

    // ── Responsable direct valide ou refuse ──
    @PutMapping("/{id}/valider-responsable")
    public ResponseEntity<Demande> validerParResponsable(
            @PathVariable Long id,
            @RequestBody ValidationDTO dto) {
        return ResponseEntity.ok(demandeService.validerParResponsable(id, dto));
    }

    // ── Chef département valide ou refuse ──
    @PutMapping("/{id}/valider-chef-departement")
    public ResponseEntity<Demande> validerParChefDepartement(
            @PathVariable Long id,
            @RequestBody ValidationDTO dto) {
        return ResponseEntity.ok(demandeService.validerParChefDepartement(id, dto));
    }

    // ── RH valide ou refuse ──
    @PutMapping("/{id}/valider-rh")
    public ResponseEntity<Demande> validerParRH(
            @PathVariable Long id,
            @RequestBody ValidationDTO dto) {
        return ResponseEntity.ok(demandeService.validerParRH(id, dto));
    }

    // ── Toutes les demandes ──
    @GetMapping
    public ResponseEntity<List<Demande>> getToutesLesDemandes() {
        return ResponseEntity.ok(demandeService.getToutesLesDemandes());
    }

    // ── Demandes d'un employé ──
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<Demande>> getDemandesParUtilisateur(
            @PathVariable Long utilisateurId) {
        return ResponseEntity.ok(demandeService.getDemandesParUtilisateur(utilisateurId));
    }

    // ── Demandes en attente ──
    @GetMapping("/en-attente")
    public ResponseEntity<List<Demande>> getDemandesEnAttente() {
        return ResponseEntity.ok(demandeService.getDemandesEnAttente());
    }

    // ── Supprimer une demande ──
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerDemande(@PathVariable Long id) {
        demandeService.supprimerDemande(id);
        return ResponseEntity.ok("Demande supprimée avec succès");
    }
}