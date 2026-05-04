package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.MessageDTO;
import com.memoire.gestionrh.Models.Message;
import com.memoire.gestionrh.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // ── Envoyer un message ──
    @PostMapping
    public ResponseEntity<Message> envoyerMessage(@RequestBody MessageDTO dto) {
        return ResponseEntity.ok(messageService.envoyerMessage(dto));
    }

    // ── Récupérer tous les messages d'un utilisateur ──
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<Message>> getMessagesParUtilisateur(
            @PathVariable Long utilisateurId) {
        return ResponseEntity.ok(messageService.getMessagesParUtilisateur(utilisateurId));
    }

    // ── Récupérer tous les messages ──
    @GetMapping
    public ResponseEntity<List<Message>> getTousLesMessages() {
        return ResponseEntity.ok(messageService.getTousLesMessages());
    }

    // ── Supprimer un message ──
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerMessage(@PathVariable Long id) {
        messageService.supprimerMessage(id);
        return ResponseEntity.ok("Message supprimé avec succès");
    }
}
