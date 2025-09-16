package ru.sshibko.AccountsManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.sshibko.AccountsManager.common.Const;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public final class AccountDto implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;

        @NotBlank(message = "link field is empty")
        @Pattern(regexp = Const.REGEX_LINK, message = "invalid link")
        @Size(max = 255)
        private String link;

        @Size(max = 1024)
        private String description;

        private LocalDateTime createdAt;

        private LocalDateTime changedAt;

        @NotBlank(message = "login is required")
        @Size(min = 3, max = 16)
        private String login;

        @NotBlank(message = "password is required")
        @Size(min = 8, max = 64)
        private String password;

        @NotBlank(message = "email is required")
        @Pattern(regexp = Const.REGEX_EMAIL, message = "invalid email")
        @Size(max = 128)
        private String email;

        //@Pattern(regexp = Const.REGEX_EMAIL, message = "invalid email")
        @Size(max = 128)
        private String emailAnother;

        @Size(max = 64)
        private String nickName;

        private boolean active;

        private Long userId;
}
