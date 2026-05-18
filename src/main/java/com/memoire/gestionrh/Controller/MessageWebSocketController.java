package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.MessageWebSocketDTO;
import com.memoire.gestionrh.DTO.NotificationWebSocketDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    // ── Envoyer un message à un utilisateur spécifique ──
    // Le client envoie vers /app/message
    @MessageMapping("/message")
    public void envoyerMessage(@Payload MessageWebSocketDTO message) {
        // Le serveur envoie vers /queue/messages-{destinataireId}
        messagingTemplate.convertAndSend(
            "/queue/messages-" + message.getDestinataireId(),
            message
        );
    }

    // ── Envoyer une notification à un utilisateur spécifique ──
    // Le client envoie vers /app/notification
    @MessageMapping("/notification")
    public void envoyerNotification(@Payload NotificationWebSocketDTO notification) {
        // Le serveur envoie vers /queue/notifications-{utilisateurId}
        messagingTemplate.convertAndSend(
            "/queue/notifications-" + notification.getUtilisateurId(),
            notification
        );
    }
}
