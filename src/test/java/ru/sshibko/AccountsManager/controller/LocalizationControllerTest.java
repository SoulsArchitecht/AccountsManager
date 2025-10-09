package ru.sshibko.AccountsManager.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sshibko.AccountsManager.service.LocalizationService;

import java.util.Locale;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test")
class LocalizationControllerTest {

    @Autowired
    private LocalizationService localizationService;

    @Test
    @DisplayName("getAllMessages() should return English Messages for default Locale")
    void getAllMessagesShouldReturnEnglishMessagesForDefaultLocale() {
        Map<String, String> messages = localizationService.getAllMessages(Locale.ENGLISH);
        assertThat(messages).containsKey("app.version.number");
        assertThat(messages.get("app.version.number")).startsWith("Version:");
    }

    @Test
    @DisplayName("getAllMessages() should return Russian Messages for Ru Locale")
    void getAllMessagesShouldReturnRussianMessagesForRuLocale() {
        Map<String, String> messages = localizationService.getAllMessages(new Locale("ru", "RU"));
        assertThat(messages).containsKey("app.version.number");
        assertThat(messages.get("app.version.number")).startsWith("Версия:");
    }
}