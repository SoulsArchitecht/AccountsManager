package ru.sshibko.AccountsManager.dto;

import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateUserInfoRequest {

    //@Size(min = 1, max = 255)
    private String firstName;

    //@Size(min = 1, max = 255)
    private String lastName;

    @Past
    private LocalDate birthDate;

    //@Size(min = 1, max = 255)
    private String country;

    private String phoneNumber;

    //@Email
    private String optionalEmail;
}
