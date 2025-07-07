package com.iron.tec.labs.ecommercejava.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    ResourceBundleMessageSource messageSource;
    @InjectMocks
    MessageServiceImpl messageService;

    @Test
    @DisplayName("Should get localized message without arguments")
    void getRequestLocalizedMessage() {

        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class))).thenReturn("message");

        messageService = new MessageServiceImpl(messageSource);
        LocaleContextHolder.setLocale(Locale.US);
        String result = messageService.getRequestLocalizedMessage("prefix","key");
        assertNotNull(result);
        assertEquals("message",result);
    }
    @Test
    @DisplayName("Should get localized message with arguments")
    void getRequestLocalizedMessageWithArg() {
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class))).thenReturn("message");
        String result = messageService.getRequestLocalizedMessage("prefix","key","arg");
        assertNotNull(result);
        assertEquals("message",result);
    }
}
