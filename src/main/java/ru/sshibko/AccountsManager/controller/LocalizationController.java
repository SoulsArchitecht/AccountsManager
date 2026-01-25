package ru.sshibko.AccountsManager.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.sshibko.AccountsManager.service.LocalizationService;

import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Localization API", description = "Localization for multilanguage API")
public class LocalizationController {

    private final LocalizationService localizationService;

    @GetMapping("/localization/messages")
    public Map<String, String> getMessages(
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage
    ) {
        Locale locale = Locale.ENGLISH;

        if (acceptLanguage != null && !acceptLanguage.isEmpty()) {
            locale = localizationService.parseLocale(acceptLanguage);
        }

        return localizationService.getAllMessages(locale);
    }
}
