package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.DemandeDTO;
import com.memoire.gestionrh.DTO.DemandeResponseDTO;
import com.memoire.gestionrh.DTO.ValidationDTO;
import com.memoire.gestionrh.Models.Demande;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import com.memoire.gestionrh.Service.DemandeService;
import com.memoire.gestionrh.Service.PdfService;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/demandes")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;
    private final UtilisateursRepository utilisateurRepository;
    private final PdfService pdfService;

    // ── Employé soumet une demande ──
    @PostMapping
    public ResponseEntity<Demande> soumettreDemande(
            @RequestBody DemandeDTO dto,
            @AuthenticationPrincipal String email) {

        Utilisateur employe = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));

        dto.setUtilisateurId(employe.getId());

        return ResponseEntity.ok(demandeService.soumettredemande(dto));
    }

    // ── Responsable direct valide ou refuse ──
    @PutMapping("/{id}/valider-responsable")
    @PreAuthorize("hasAnyAuthority('ROLE_RESPONSABLE', 'ROLE_MANAGER')")
    public ResponseEntity<Demande> validerParResponsable(
            @PathVariable UUID id,
            @RequestBody ValidationDTO dto) {

        return ResponseEntity.ok(demandeService.validerParResponsable(id, dto));
    }

    // ── Chef de département valide ou refuse ──
    @PutMapping("/{id}/valider-chef-departement")
    @PreAuthorize("hasAnyAuthority('ROLE_CHEF DÉPARTEMENT', 'ROLE_CHEF DEPARTEMENT', 'ROLE_CHEF_DEPARTEMENT')")
    public ResponseEntity<Demande> validerParChefDepartement(
            @PathVariable UUID id,
            @RequestBody ValidationDTO dto) {

        return ResponseEntity.ok(demandeService.validerParChefDepartement(id, dto));
    }

    // ── RH valide ou refuse ──
    @PutMapping("/{id}/valider-rh")
    @PreAuthorize("hasAnyAuthority('ROLE_RH')")
    public ResponseEntity<Demande> validerParRH(
            @PathVariable UUID id,
            @RequestBody ValidationDTO dto) {

        return ResponseEntity.ok(demandeService.validerParRH(id, dto));
    }

    // ── Toutes les demandes ──
    @GetMapping
    public ResponseEntity<List<DemandeResponseDTO>> getToutesLesDemandes() {
        return ResponseEntity.ok(
                demandeService.getToutesLesDemandes()
                        .stream()
                        .map(DemandeResponseDTO::from)
                        .toList());
    }

    // ── Demandes d'un employé ──
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<Demande>> getDemandesParUtilisateur(
            @PathVariable UUID utilisateurId) {

        return ResponseEntity.ok(demandeService.getDemandesParUtilisateur(utilisateurId));
    }

    // ── Demandes en attente ──
    @GetMapping("/en-attente")
    public ResponseEntity<List<DemandeResponseDTO>> getDemandesEnAttente() {
        return ResponseEntity.ok(
                demandeService.getDemandesEnAttente()
                        .stream()
                        .map(DemandeResponseDTO::from)
                        .toList());
    }

    // ── Supprimer une demande ──
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerDemande(@PathVariable UUID id) {

        demandeService.supprimerDemande(id);
        return ResponseEntity.ok("Demande supprimée avec succès");
    }

    // ── Demandes de l'utilisateur connecté (depuis le token JWT) ──
    @GetMapping("/mes-demandes")
    public ResponseEntity<List<DemandeResponseDTO>> getMesDemandes(
            @AuthenticationPrincipal String email) {

        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return ResponseEntity.ok(
                demandeService.getDemandesParUtilisateur(utilisateur.getId())
                        .stream()
                        .map(DemandeResponseDTO::from)
                        .toList());
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> telechargerPdf(@PathVariable UUID id) throws Exception {

        byte[] pdf = pdfService.genererPdfDemande(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=demande-" + id + ".pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }

    // ── Modifier une demande ──
    @PutMapping("/{id}")
public ResponseEntity<Demande> modifierDemande(
        @PathVariable UUID id,
        @RequestBody DemandeDTO dto) {

    return ResponseEntity.ok(demandeService.modifierDemande(id, dto));
}

}