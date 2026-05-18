package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

    List<Message> findBySender_Id(Long id);

    List<Message> findByReceiver_Id(Long id);

}