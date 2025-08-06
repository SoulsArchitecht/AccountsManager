package ru.sshibko.AccountsManager.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class UserInfoDto implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String country;
    private String phoneNumber;
    private String optionalEmail;
    private String avatarUrl;
    private LocalDateTime registrationDate;
}
