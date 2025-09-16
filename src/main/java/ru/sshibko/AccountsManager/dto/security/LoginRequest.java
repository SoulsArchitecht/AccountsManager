package ru.sshibko.AccountsManager.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Email cannot be null")
    @Email(message = "email must be in correct form")
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8)
    private String password;
}
