package ru.sshibko.AccountsManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sshibko.AccountsManager.configuration.AppInfoProperties;
import ru.sshibko.AccountsManager.dto.AppInfoDto;

@RestController
@RequiredArgsConstructor
@Tag(name = "Application info", description = "Application public information")
public class AppInfoController {

    private final AppInfoProperties appInfoProperties;

    @GetMapping("/app-info")
    @Operation(summary = "Get Application info")
    public AppInfoDto getAppInfo(){
        var contact = appInfoProperties.getContact();
        var version = appInfoProperties.getVersion();

        return AppInfoDto.builder()
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .telegram(contact.getTelegram())
                .versionNumber(version.getNumber())
                .versionStatus(version.getStatus())
                .build();
    }
}
