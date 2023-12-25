package ru.sshibko.AccountsManager.dto;

import ru.sshibko.AccountsManager.model.entity.User;
import java.time.LocalDateTime;

public record UrlDto(
        Long id,
        String link,
        String description,
        LocalDateTime createdAt,
        LocalDateTime changedAt,
        String login,
        String password,
        String email,
        String emailAnother,
        String nickname,
        boolean active,
        User user
) {
}
