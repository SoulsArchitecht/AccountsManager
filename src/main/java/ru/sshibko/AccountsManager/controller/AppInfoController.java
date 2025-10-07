package ru.sshibko.AccountsManager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sshibko.AccountsManager.configuration.AppInfoProperties;
import ru.sshibko.AccountsManager.dto.AppInfoDto;

@RestController
@RequiredArgsConstructor
public class AppInfoController {

    private final AppInfoProperties appInfoProperties;

    @GetMapping("/app-info")
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
