package ru.sshibko.AccountsManager.dto;

import java.time.LocalDateTime;

public record UrlDto(
        String link,
        String description,
        LocalDateTime createdAt,
        LocalDateTime changedAt,
        String login,
        String password,
        String email,
        String emailAnother,
        String nickname,
        boolean active) {
}
