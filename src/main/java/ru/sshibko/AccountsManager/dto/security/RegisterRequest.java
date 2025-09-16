package ru.sshibko.AccountsManager.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Email cannot be null")
    @Email(message = "email must be in correct form")
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, message = "Password minimum length is 8")
    private String password;

    @NotBlank(message = "Nickname cannot be null")
    @Size(min = 1, max = 255, message = "Nickname length must be between 1 and 255")
    private String login;
}
