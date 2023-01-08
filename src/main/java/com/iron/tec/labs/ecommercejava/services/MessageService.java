package com.iron.tec.labs.ecommercejava.services;

public interface MessageService {
    String getRequestLocalizedMessage(String prefix, String key);

    String getRequestLocalizedMessage(String prefix, String key, String... params);
}
