package ru.sshibko.AccountsManager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.sshibko.AccountsManager.model.entity.Role;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "User's private info")
public final class UserDto implements Serializable {

    @Schema(description = "User's ID", example = "1")
    private Long id;

    @Schema(description = "User's private registration email", example = "user@gmail.com")
    private String email;

    @Schema(description = "User's nickname", example = "White Horse")
    private String login;

    @Schema(description = "User's unique strong password", example = "20!38Ga7IjTZa")
    private String password;

    @Schema(description = "Status of this User", example = "true")
    private boolean status;

    @Schema(description = "System role and access rights for this User", example = "ROLE_USER")
    private Role role;
}
