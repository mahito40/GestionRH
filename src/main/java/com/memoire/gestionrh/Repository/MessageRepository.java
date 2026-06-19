package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationIdOrderByDateEnvoiAsc(UUID conversationId);

    // Messages non lus (envoyés par l'autre participant)
    List<Message> findByConversationIdAndLuFalseAndSenderIdNot(UUID conversationId, UUID senderId);


    List<Message> findBySender_Id(UUID id);

    List<Message> findByReceiver_Id(UUID id);

}