package com.iron.tec.labs.ecommercejava.services;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmailInParallel(String to, String subject, String body);
    Boolean sendMailSync(String to, String subject, String body) throws MessagingException;
}
