package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Notification;
import com.memoire.gestionrh.enums.StatutNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Récupérer toutes les notifications d'un utilisateur
    List<Notification> findByUtilisateurId(Long utilisateurId);
    // Récupérer les notifications non lues d'un utilisateur
    List<Notification> findByUtilisateurIdAndStatut(Long utilisateurId, StatutNotification statut);
}
