package ru.sshibko.AccountsManager.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User's registration request")
public class RegisterRequest {

    @NotBlank(message = "Email cannot be null")
    @Email(message = "email must be in correct form")
    @Schema(description = "User's email address", example = "user@gmail.com" )
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, message = "Password minimum length is 8")
    @Schema(description = "User's unique strong password", example = "20!38Ga7IjTZa")
    private String password;

    @NotBlank(message = "Nickname cannot be null")
    @Size(min = 1, max = 255, message = "Nickname length must be between 1 and 255")
    @Schema(description = "User's nickname", example = "White Horse")
    private String login;
}
