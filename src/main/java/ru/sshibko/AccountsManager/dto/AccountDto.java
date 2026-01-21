package ru.sshibko.AccountsManager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Account info")
public final class AccountDto implements Serializable {

        @Schema(description = "Account ID", example = "1")
        private Long id;

        @NotBlank(message = "link field is empty")
        @Pattern(regexp = Const.REGEX_LINK, message = "invalid link")
        @Size(max = 255)
        @Schema(description = "URL of Internet Resource", example = "https:\\microsoft.com")
        private String link;

        @Size(max = 1024)
        @Schema(description = "Description for an Internet Resource", example = "Soft producer")
        private String description;

        @Schema(description = "Account creation date and time", example = "2025-10-09T18:25:58.6037597" )
        private LocalDateTime createdAt;

        @Schema(description = "Account last update date and time", example = "2025-10-09T18:25:58.6037597" )
        private LocalDateTime changedAt;

        @NotBlank(message = "login is required")
        @Size(min = 3, max = 16)
        @Schema(description = "User's login for this Account", example = "Peter_Wolf")
        private String login;

        @NotBlank(message = "password is required")
        @Size(min = 8, max = 64)
        @Schema(description = "User's password for this Account", example = "20!38Ga7IjTZa")
        private String password;

        @NotBlank(message = "email is required")
        @Pattern(regexp = Const.REGEX_EMAIL, message = "invalid email")
        @Size(max = 128)
        @Schema(description = "User's email address for this Account", example = "user@gmail.com")
        private String email;

        //@Pattern(regexp = Const.REGEX_EMAIL, message = "invalid email")
        @Size(max = 128)
        @Schema(description = "Optional user's email address for this Account", example = "user@mail.ru")
        private String emailAnother;

        @Size(max = 64)
        @Schema(description = "User's visible nickname", example = "Black wolf")
        private String nickName;

        @Schema(description = "Status of this Account", example = "true")
        private boolean active;

        @Schema(description = "User's ID who owns the account", example = "1")
        private Long userId;
}
