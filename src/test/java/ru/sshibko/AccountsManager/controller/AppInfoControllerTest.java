package ru.sshibko.AccountsManager.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import ru.sshibko.AccountsManager.configuration.AppInfoProperties;
import ru.sshibko.AccountsManager.dto.AppInfoDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test")
class AppInfoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppInfoProperties appInfoProperties;

    @Test
    @DisplayName("getAppInfo() should return appInfoProperties")
    void getAppInfoShouldReturnAppInfoProperties() {
        ResponseEntity<AppInfoDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/app-info", AppInfoDto.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        AppInfoDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getEmail()).isEqualTo(appInfoProperties.getContact().getEmail());
        assertThat(body.getPhone()).isEqualTo(appInfoProperties.getContact().getPhone());
        assertThat(body.getTelegram()).isEqualTo(appInfoProperties.getContact().getTelegram());
        assertThat(body.getVersionNumber()).isEqualTo(appInfoProperties.getVersion().getNumber());
        assertThat(body.getVersionStatus()).isEqualTo(appInfoProperties.getVersion().getStatus());
    }
}