package ru.sshibko.AccountsManager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "User's public info")
public class UserInfoDto implements Serializable {

    @Schema(description = "User's ID", example = "1")
    private Long id;

    @Schema(description = "User's firstname", example = "John")
    private String firstName;

    @Schema(description = "User's lastname", example = "Smith")
    private String lastName;

    @Schema(description = "User's date of birth", example = "2005-10-09")
    private LocalDate birthDate;

    @Schema(description = "User's country", example = "USA")
    private String country;

    @Schema(description = "User's phone number", example = "+78324432244")
    private String phoneNumber;

    @Schema(description = "User's optional public email", example = "user_free@gmail.com")
    private String optionalEmail;

    @Schema(description = "link for user's avatar uploading", example = "https:\\myphoto.com")
    private String avatarUrl;

    @Schema(description = "User's registration date and time", example = "2025-10-09T18:25:58.6037597")
    private LocalDateTime registrationDate;
}
