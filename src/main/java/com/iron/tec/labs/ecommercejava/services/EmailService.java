package com.iron.tec.labs.ecommercejava.services;

import jakarta.mail.MessagingException;
import reactor.core.Disposable;

public interface EmailService {
    Disposable sendEmailInParallel(String to, String subject, String body);
    Boolean sendMailSync(String to, String subject, String body) throws MessagingException;
}
