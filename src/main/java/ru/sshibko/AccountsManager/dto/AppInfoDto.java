package ru.sshibko.AccountsManager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Application info")
public class AppInfoDto {

    @Schema(description = "Developer's contact email", example = "user@gmail.com")
    private String email;

    @Schema(description = "Developer's contact phone", example = "+79523926622")
    private String phone;

    @Schema(description = "Developer's contact telegram nickname", example = "@developer")
    private String telegram;

    @Schema(description = "Application version number", example = "1.0.0")
    private String versionNumber;

    @Schema(description = "Application version status", example = "Test")
    private String versionStatus;
}
