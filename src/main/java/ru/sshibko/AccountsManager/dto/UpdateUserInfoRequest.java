package ru.sshibko.AccountsManager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Schema(description = "User information for update")
public class UpdateUserInfoRequest {

    //@Size(min = 1, max = 255)
    @Schema(description = "User's firstname", example = "John")
    private String firstName;

    //@Size(min = 1, max = 255)
    @Schema(description = "User's lastname", example = "Smith")
    private String lastName;

    @Past
    @Schema(description = "User's date of birth", example = "2005-10-09")
    private LocalDate birthDate;

    //@Size(min = 1, max = 255)
    @Schema(description = "User's country", example = "USA")
    private String country;

    @Schema(description = "User's phone number", example = "+78324432244")
    private String phoneNumber;

    //@Email
    @Schema(description = "User's optional public email", example = "user_free@gmail.com")
    private String optionalEmail;
}
