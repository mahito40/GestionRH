package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.Models.Notification;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Repository.NotificationRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import com.memoire.gestionrh.enums.StatutNotification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateursRepository utilisateurRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                               UtilisateursRepository utilisateurRepository,    
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void envoyerNotification(Long utilisateurId, String contenu) {
    Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    Notification notif = new Notification();
    notif.setContenu(contenu);
    notif.setDatenotif(LocalDateTime.now());
    notif.setStatut(StatutNotification.NON_LU);
    notif.setUtilisateur(utilisateur);

    Notification saved = notificationRepository.save(notif);

    // ← envoie en temps réel via WebSocket
    messagingTemplate.convertAndSend(
        "/queue/notifications-" + utilisateurId,
        saved
    );
}

    public List<Notification> getNotificationsParUtilisateur(Long utilisateurId) {
        return notificationRepository.findByUtilisateurId(utilisateurId);
    }
}