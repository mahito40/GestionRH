package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.NotificationWebSocketDTO;
import com.memoire.gestionrh.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // GET /api/notifications/{utilisateurId}
    @GetMapping("/{utilisateurId}")
    public ResponseEntity<List<NotificationWebSocketDTO>> getNotifications(@PathVariable UUID utilisateurId) {
        return ResponseEntity.ok(notificationService.getNotifications(utilisateurId));
    }

    // GET /api/notifications/{utilisateurId}/non-lues
    @GetMapping("/{utilisateurId}/non-lues")
    public ResponseEntity<List<NotificationWebSocketDTO>> getNonLues(@PathVariable UUID utilisateurId) {
        return ResponseEntity.ok(notificationService.getNotificationsNonLues(utilisateurId));
    }

    // GET /api/notifications/{utilisateurId}/count
    @GetMapping("/{utilisateurId}/count")
    public ResponseEntity<Long> getCount(@PathVariable UUID utilisateurId) {
        return ResponseEntity.ok(notificationService.getNombreNonLues(utilisateurId));
    }

    // PATCH /api/notifications/{id}/lue
    @PatchMapping("/{id}/lue")
    public ResponseEntity<NotificationWebSocketDTO> marquerCommeLue(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.marquerCommeLue(id));
    }

    // PATCH /api/notifications/{utilisateurId}/toutes-lues
    @PatchMapping("/{utilisateurId}/toutes-lues")
    public ResponseEntity<Void> marquerToutesCommeLues(@PathVariable UUID utilisateurId) {
        notificationService.marquerToutesCommeLues(utilisateurId);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/notifications/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable UUID id) {
        notificationService.supprimerNotification(id);
        return ResponseEntity.noContent().build();
    }
}
