package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Conversation;
import com.memoire.gestionrh.enums.StatutConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    // Toutes les conversations d'un utilisateur (expediteur ou destinataire)
    @Query("SELECT c FROM Conversation c WHERE c.expediteur.id = :userId OR c.destinataire.id = :userId ORDER BY c.lastMessage DESC")
    List<Conversation> findByParticipantId(@Param("userId") UUID userId);

    // Conversation entre deux utilisateurs précis
    @Query("SELECT c FROM Conversation c WHERE (c.expediteur.id = :p1 AND c.destinataire.id = :p2) OR (c.expediteur.id = :p2 AND c.destinataire.id = :p1)")
    Optional<Conversation> findByParticipants(@Param("p1") UUID expediteurId, @Param("p2") UUID destinataireId);

    // Conversations par statut pour un utilisateur
    @Query("SELECT c FROM Conversation c WHERE (c.expediteur.id = :userId OR c.destinataire.id = :userId) AND c.statut = :statut")
    List<Conversation> findByParticipantIdAndStatut(@Param("userId") UUID userId, @Param("statut") StatutConversation statut);

    // Vérifie si une conversation existe déjà entre deux utilisateurs
    @Query("SELECT COUNT(c) > 0 FROM Conversation c WHERE (c.expediteur.id = :p1 AND c.destinataire.id = :p2) OR (c.expediteur.id = :p2 AND c.destinataire.id = :p1)")
    boolean existsByParticipants(@Param("p1") UUID expediteurId, @Param("p2") UUID destinataireId);
}
