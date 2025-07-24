package ru.sshibko.AccountsManager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateUserInfoRequest {

    //@NotBlank
    //@Size(min = 1, max = 255)
    private String firstName;

    //@NotBlank
    //@Size(min = 1, max = 255)
    private String lastName;

    @Past
    private LocalDate birthDate;

    //@NotBlank
    //@Size(min = 1, max = 255)
    private String country;

    //TODO anno filter
    private String phoneNumber;

    //@Email
    private String optionalEmail;
}
