package ru.sshibko.AccountsManager.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Authentication login request")
public class LoginRequest {

    @NotBlank(message = "Email cannot be null")
    @Email(message = "email must be in correct form")
    @Schema(description = "User's email address", example = "user@gmail.com" )
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8)
    @Schema(description = "User's unique strong password", example = "20!38Ga7IjTZa")
    private String password;
}
