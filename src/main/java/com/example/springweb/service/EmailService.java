package com.example.springweb.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {
    private final   String to = "springweb9@gmail.com";
    private final String from = "springweb9@gmail.com";
    private final String password = "fjhu gilo rxod qjpz";
    private final String host = "smtp.gmail.com";
    private final String smtpPort = "465";


    public void sendPasswordResetEmail(String to, String resetLink) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Password Reset Request");
            message.setText("Click the following link to reset your password: " + resetLink);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}