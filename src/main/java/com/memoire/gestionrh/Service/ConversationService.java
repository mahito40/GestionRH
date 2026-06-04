package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.ConversationDTO;
import com.memoire.gestionrh.Models.Conversation;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Repository.ConversationRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import com.memoire.gestionrh.enums.StatutConversation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UtilisateursRepository utilisateurRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               UtilisateursRepository utilisateurRepository) {
        this.conversationRepository = conversationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // Créer une nouvelle conversation
    public ConversationDTO creerConversation(UUID expediteurId, UUID destinataireId) {
        if (conversationRepository.existsByParticipants(expediteurId, destinataireId)) {
            throw new IllegalStateException("Une conversation existe déjà entre ces deux utilisateurs.");
        }

        Utilisateur p1 = utilisateurRepository.findById(expediteurId)
                .orElseThrow(() -> new EntityNotFoundException("Participant 1 introuvable"));
        Utilisateur p2 = utilisateurRepository.findById(destinataireId)
                .orElseThrow(() -> new EntityNotFoundException("Participant 2 introuvable"));

        Conversation conversation = new Conversation();
        conversation.setexpediteur(p1);
        conversation.setdestinataire(p2);
        conversation.setStatut(StatutConversation.active);

        return toDTO(conversationRepository.save(conversation));
    }

    // Récupérer toutes les conversations d'un utilisateur
    @Transactional(readOnly = true)
    public List<ConversationDTO> getConversationsParUtilisateur(UUID userId) {
        return conversationRepository.findByParticipantId(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Récupérer une conversation par ID
    @Transactional(readOnly = true)
    public ConversationDTO getConversationParId(UUID id) {
        return conversationRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable avec l'id : " + id));
    }

    // Récupérer la conversation entre deux utilisateurs
    @Transactional(readOnly = true)
    public ConversationDTO getConversationEntreParticipants(UUID p1Id, UUID p2Id) {
        return conversationRepository.findByParticipants(p1Id, p2Id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aucune conversation trouvée entre ces participants."));
    }

    // Changer le statut
    public ConversationDTO changerStatut(UUID id, StatutConversation nouveauStatut) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable"));
        conversation.setStatut(nouveauStatut);
        return toDTO(conversationRepository.save(conversation));
    }

    // Supprimer
    public void supprimerConversation(UUID id) {
        if (!conversationRepository.existsById(id)) {
            throw new EntityNotFoundException("Conversation introuvable avec l'id : " + id);
        }
        conversationRepository.deleteById(id);
    }

    // Mapper Conversation → DTO
    private ConversationDTO toDTO(Conversation c) {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(c.getId());
        dto.setStatut(c.getStatut());
        dto.setDateCreation(c.getDateCreation());
        dto.setLastMessage(c.getLastMessage());
        dto.setexpediteurId(c.getexpediteur().getId());
        dto.setexpediteurNom(c.getexpediteur().getNom()); // adapte selon ton getter
        dto.setdestinataireId(c.getdestinataire().getId());
        dto.setdestinataireNom(c.getdestinataire().getNom());
        return dto;
    }
}