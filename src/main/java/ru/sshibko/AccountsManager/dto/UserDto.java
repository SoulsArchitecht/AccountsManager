package ru.sshibko.AccountsManager.dto;

import lombok.*;
import ru.sshibko.AccountsManager.model.entity.Role;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public final class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String login;
    private String password;
    private boolean status;
    private Role role;
}
