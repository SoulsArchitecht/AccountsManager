package ru.sshibko.AccountsManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountDto {

        private Long id;
        private String link;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime changedAt;
        private String login;
        private String password;
        private String email;
        private String emailAnother;
        private String nickName;
        private boolean active;
        private Long userId;
}
