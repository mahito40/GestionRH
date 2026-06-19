package com.memoire.gestionrh.Repository;

import com.memoire.gestionrh.Models.Notification;
import com.memoire.gestionrh.enums.StatutNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    // Toutes les notifications d'un utilisateur
    List<Notification> findByUtilisateurIdOrderByDatenotifDesc(UUID utilisateurId);

    // Notifications non lues d'un utilisateur
    List<Notification> findByUtilisateurIdAndStatut(UUID utilisateurId, StatutNotification statut);

    // Nombre de notifications non lues
    long countByUtilisateurIdAndStatut(UUID utilisateurId, StatutNotification statut);
}