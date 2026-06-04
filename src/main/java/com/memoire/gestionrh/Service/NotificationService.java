package com.memoire.gestionrh.Service;

import com.memoire.gestionrh.DTO.NotificationWebSocketDTO;
import com.memoire.gestionrh.Models.Notification;
import com.memoire.gestionrh.Models.Utilisateur;
import com.memoire.gestionrh.Repository.NotificationRepository;
import com.memoire.gestionrh.Repository.UtilisateursRepository;
import com.memoire.gestionrh.enums.StatutNotification;
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
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateursRepository utilisateursRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Créer et envoyer une notification en temps réel
    public NotificationWebSocketDTO creerNotification(UUID utilisateurId, String contenu) {
        Utilisateur utilisateur = utilisateursRepository.findById(utilisateurId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        Notification notification = new Notification();
        notification.setContenu(contenu);
        notification.setDatenotif(LocalDateTime.now());
        notification.setStatut(StatutNotification.NON_LU);
        notification.setUtilisateur(utilisateur);

        Notification saved = notificationRepository.save(notification);
        NotificationWebSocketDTO dto = toDTO(saved);

        // Push en temps réel vers l'utilisateur concerné
        messagingTemplate.convertAndSendToUser(
                utilisateurId.toString(),
                "/queue/notifications",
                dto
        );

        return dto;
    }

    // Récupérer toutes les notifications d'un utilisateur
    @Transactional(readOnly = true)
    public List<NotificationWebSocketDTO> getNotifications(UUID utilisateurId) {
        return notificationRepository
                .findByUtilisateurIdOrderByDatenotifDesc(utilisateurId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Récupérer les notifications non lues
    @Transactional(readOnly = true)
    public List<NotificationWebSocketDTO> getNotificationsNonLues(UUID utilisateurId) {
        return notificationRepository
                .findByUtilisateurIdAndStatut(utilisateurId, StatutNotification.NON_LU)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Nombre de notifications non lues
    @Transactional(readOnly = true)
    public long getNombreNonLues(UUID utilisateurId) {
        return notificationRepository.countByUtilisateurIdAndStatut(
                utilisateurId, StatutNotification.NON_LU);
    }

    // Marquer une notification comme lue
    public NotificationWebSocketDTO marquerCommeLue(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification introuvable"));
        notification.setStatut(StatutNotification.LU);
        return toDTO(notificationRepository.save(notification));
    }

    // Marquer toutes les notifications d'un utilisateur comme lues
    public void marquerToutesCommeLues(UUID utilisateurId) {
        List<Notification> nonLues = notificationRepository
                .findByUtilisateurIdAndStatut(utilisateurId, StatutNotification.NON_LU);
        nonLues.forEach(n -> n.setStatut(StatutNotification.LU));
        notificationRepository.saveAll(nonLues);
    }

    // Supprimer une notification
    public void supprimerNotification(UUID id) {
        notificationRepository.deleteById(id);
    }

    private NotificationWebSocketDTO toDTO(Notification n) {
        NotificationWebSocketDTO dto = new NotificationWebSocketDTO();
        dto.setId(n.getId());
        dto.setContenu(n.getContenu());
        dto.setDatenotif(n.getDatenotif());
        dto.setStatut(n.getStatut());
        dto.setUtilisateurId(n.getUtilisateur().getId());
        return dto;
    }
    // Alias utilisé par DemandeService
public void envoyerNotification(UUID utilisateurId, String contenu) {
    creerNotification(utilisateurId, contenu);
}
}