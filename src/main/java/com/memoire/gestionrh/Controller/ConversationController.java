package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.ConversationDTO;
import com.memoire.gestionrh.DTO.ConversationRequest;
import com.memoire.gestionrh.Service.ConversationService;
import com.memoire.gestionrh.enums.StatutConversation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    // POST /api/conversations?expediteurId=...&destinataireId=...&titre=...
   @PostMapping
public ResponseEntity<ConversationDTO> creerConversation(@RequestBody ConversationRequest request) {
    ConversationDTO dto = conversationService.creerConversation(
        request.getExpediteurId(),
        request.getDestinataireId()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
}
    // GET /api/conversations/utilisateur/{userId}
    @GetMapping("/utilisateur/{userId}")
    public ResponseEntity<List<ConversationDTO>> getConversationsParUtilisateur(@PathVariable UUID userId) {
        return ResponseEntity.ok(conversationService.getConversationsParUtilisateur(userId));
    }

    // GET /api/conversations/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getConversationParId(@PathVariable UUID id) {
        return ResponseEntity.ok(conversationService.getConversationParId(id));
    }

    // GET /api/conversations/entre?p1=...&p2=...
    @GetMapping("/entre")
    public ResponseEntity<ConversationDTO> getConversationEntreParticipants(
            @RequestParam UUID p1,
            @RequestParam UUID p2) {
        return ResponseEntity.ok(conversationService.getConversationEntreParticipants(p1, p2));
    }

    // PATCH /api/conversations/{id}/statut?nouveauStatut=archivee
    @PatchMapping("/{id}/statut")
    public ResponseEntity<ConversationDTO> changerStatut(
            @PathVariable UUID id,
            @RequestParam StatutConversation nouveauStatut) {
        return ResponseEntity.ok(conversationService.changerStatut(id, nouveauStatut));
    }

    // DELETE /api/conversations/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerConversation(@PathVariable UUID id) {
        conversationService.supprimerConversation(id);
        return ResponseEntity.noContent().build();
    }

    
}
