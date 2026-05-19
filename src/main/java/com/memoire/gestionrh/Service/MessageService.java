package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.MessageDTO;
import com.memoire.gestionrh.Models.Message;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Repository.MessageRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateursRepository utilisateursRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // ── Envoyer un message ──
    public Message envoyerMessage(MessageDTO dto) {
        Utilisateur utilisateur = utilisateursRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Message message = new Message();
        message.setContenu(dto.getContenu());
        message.setDateEnvoi(LocalDateTime.now());
        message.setSender(utilisateur);

        Message saved = messageRepository.save(message);

        // ← notifie en temps réel via WebSocket
        messagingTemplate.convertAndSend(
                "/queue/messages-" + dto.getUtilisateurId(),
                saved);

        return saved;
    }

    // ── Récupérer tous les messages d'un utilisateur ──
    public List<Message> getMessagesParUtilisateur(UUID utilisateurId) {
        return messageRepository.findBySender_Id(utilisateurId);
    }

    // ── Récupérer tous les messages ──
    public List<Message> getTousLesMessages() {
        return messageRepository.findAll();
    }

    // ── Supprimer un message ──
    public void supprimerMessage(UUID id) {
        messageRepository.deleteById(id);
    }
}
