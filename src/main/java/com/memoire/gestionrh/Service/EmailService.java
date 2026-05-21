package com.memoire.gestionrh.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void envoyerCodeProvisoire(String email, String nom, String codeProvisoire) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Bienvenue sur HOMINTEC RH - Vos identifiants de connexion");
        message.setText(
            "Bonjour " + nom + ",\n\n" +
            "Votre compte a été créé sur la plateforme RH HOMINTEC.\n\n" +
            "Voici vos identifiants de connexion :\n" +
            "Email     : " + email + "\n" +
            "Code provisoire : " + codeProvisoire + "\n\n" +
            "Vous devez changer votre mot de passe obligatoirement à la première connexion.\n\n" +
            "Cordialement,\n" +
            "L'équipe RH HOMINTEC"
        );
        mailSender.send(message);
    }
}
