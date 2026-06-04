package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.MessageDTO;
import com.memoire.gestionrh.Models.Conversation;
import com.memoire.gestionrh.Models.Message;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Repository.ConversationRepository;
import com.memoire.gestionrh.Repository.MessageRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UtilisateursRepository utilisateursRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // ── Envoyer un message ──
    public MessageDTO envoyerMessage(MessageDTO dto) {
        Conversation conversation = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable"));

        Utilisateur expediteur = utilisateursRepository.findById(dto.getExpediteurId())
                .orElseThrow(() -> new EntityNotFoundException("Expéditeur introuvable"));

        Message message = new Message();
        message.setContenu(dto.getContenu());
        message.setSender(expediteur);
        message.setConversation(conversation);

        conversation.setLastMessage(LocalDateTime.now());
        conversationRepository.save(conversation);

        Message saved = messageRepository.save(message);
        MessageDTO response = toDTO(saved);

        messagingTemplate.convertAndSend(
                "/topic/conversation/" + dto.getConversationId(),
                response
        );

        return response;
    }

    // ── Historique d'une conversation ──
    @Transactional(readOnly = true)
    public List<MessageDTO> getMessages(UUID conversationId) {
        return messageRepository.findByConversationIdOrderByDateEnvoiAsc(conversationId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ── Messages d'un utilisateur ──
    @Transactional(readOnly = true)
    public List<MessageDTO> getMessagesParUtilisateur(UUID utilisateurId) {
        return messageRepository.findBySender_Id(utilisateurId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ── Tous les messages ──
    @Transactional(readOnly = true)
    public List<MessageDTO> getTousLesMessages() {
        return messageRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ── Marquer comme lus ──
    public void marquerCommeLus(UUID conversationId, UUID userId) {
        List<Message> nonLus = messageRepository
                .findByConversationIdAndLuFalseAndSenderIdNot(conversationId, userId);
        nonLus.forEach(m -> m.setLu(true));
        messageRepository.saveAll(nonLus);

        messagingTemplate.convertAndSend(
                "/topic/conversation/" + conversationId + "/lu",
                userId
        );
    }

    // ── Supprimer un message ──
    public void supprimerMessage(UUID id) {
        messageRepository.deleteById(id);
    }

    // ── Mapper Message → DTO ──
    private MessageDTO toDTO(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setContenu(m.getContenu());
        dto.setDateEnvoi(m.getDateEnvoi());
        dto.setConversationId(m.getConversation().getId());
        dto.setExpediteurId(m.getSender().getId());
        dto.setExpediteurNom(m.getSender().getNom()); // adapte selon ton getter
        dto.setLu(m.isLu());
        return dto;
    }
}