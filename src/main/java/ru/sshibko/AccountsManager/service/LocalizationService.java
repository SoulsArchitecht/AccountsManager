package ru.sshibko.AccountsManager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalizationService {

    private final MessageSource messageSource;

    public Map<String, String> getAllMessages(Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        Map<String, String> messages = new HashMap<>();
        for (String key : bundle.keySet()) {
            messages.put(key, messageSource.getMessage(key, null, locale));
        }

        return messages;
/*        return Collections.list(bundle.getKeys()).stream()
                .collect(Collectors.toMap(k -> k, bundle::getString));*/
    }

    public Locale parseLocale(String acceptLanguage) {
        String[] parts = acceptLanguage.split(",");
        String langTag = parts[0].split(";")[0].trim();
        return Locale.forLanguageTag(langTag);
    }
}
