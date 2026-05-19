package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findBySender_Id(UUID id);

    List<Message> findByReceiver_Id(UUID id);

}