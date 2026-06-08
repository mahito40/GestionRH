package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.MessageDTO;
import com.memoire.gestionrh.DTO.MessageRequest;
import com.memoire.gestionrh.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // ── WebSocket : client envoie vers /app/message.envoyer ──
    @MessageMapping("/message.envoyer")
    public void envoyerViaSocket(@Payload MessageRequest request) {
        messageService.envoyerMessage(request);
    }

    // ── Envoyer un message (REST) ──
    @PostMapping
    public ResponseEntity<MessageDTO> envoyerMessage(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.envoyerMessage(request));
    }

    // ── Historique des messages d'une conversation ──
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable UUID conversationId) {
        return ResponseEntity.ok(messageService.getMessages(conversationId));
    }

    // ── Récupérer tous les messages d'un utilisateur ──
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<MessageDTO>> getMessagesParUtilisateur(@PathVariable UUID utilisateurId) {
        return ResponseEntity.ok(messageService.getMessagesParUtilisateur(utilisateurId));
    }

    // ── Récupérer tous les messages ──
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getTousLesMessages() {
        return ResponseEntity.ok(messageService.getTousLesMessages());
    }

    // ── Marquer les messages d'une conversation comme lus ──
    @PatchMapping("/conversation/{conversationId}/lu")
    public ResponseEntity<Void> marquerCommeLus(
            @PathVariable UUID conversationId,
            @RequestParam UUID userId) {
        messageService.marquerCommeLus(conversationId, userId);
        return ResponseEntity.noContent().build();
    }

    // ── Supprimer un message ──
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerMessage(@PathVariable UUID id) {
        messageService.supprimerMessage(id);
        return ResponseEntity.ok("Message supprimé avec succès");
    }
}