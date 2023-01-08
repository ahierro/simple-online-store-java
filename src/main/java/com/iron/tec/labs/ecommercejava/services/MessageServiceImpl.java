package com.iron.tec.labs.ecommercejava.services;

import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ResourceBundleMessageSource messageSource;

    @Override
    public String getRequestLocalizedMessage(String prefix, String key) {
        return messageSource.getMessage(concatPrefixAndKey(prefix, key), new ArrayList<>().toArray(), LocaleContextHolder.getLocale());
    }

    @Override
    public String getRequestLocalizedMessage(String prefix, String key, String... params) {
        return messageSource.getMessage(concatPrefixAndKey(prefix, key), params, LocaleContextHolder.getLocale());
    }
    private String concatPrefixAndKey(String prefix, String key) {
        return prefix.concat(".").concat(key);
    }
}