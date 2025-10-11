package ru.sshibko.AccountsManager.service;


import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LocalizationServiceTest {

    @Test
    void getAllMessages_shouldReturnEnglishMessagesForEnglishLocale() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        LocalizationService service = new LocalizationService(messageSource);

        Map<String, String> messages = service.getAllMessages(Locale.ENGLISH);

        assertThat(messages).containsKey("app.version.number");
        assertThat(messages.get("app.version.number")).startsWith("Version:");
        assertThat(messages).containsKey("auth.password");
        assertThat(messages.get("auth.password")).isEqualTo("Password");
    }

    @Test
    void getAllMessages_shouldReturnRussianMessagesForRussianLocale() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        LocalizationService service = new LocalizationService(messageSource);

        Map<String, String> messages = service.getAllMessages(new Locale("ru"));

        assertThat(messages).containsKey("app.version.number");
        assertThat(messages.get("app.version.number")).startsWith("Версия:");
        assertThat(messages).containsKey("auth.password");
        assertThat(messages.get("auth.password")).isEqualTo("Пароль");
    }

    @Test
    void parseLocale_shouldParseAcceptLanguageHeader() {
        LocalizationService service = new LocalizationService(null); // не используется в этом методе

        Locale locale = service.parseLocale("ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        assertThat(locale).isEqualTo(new Locale("ru", "RU"));
    }
}