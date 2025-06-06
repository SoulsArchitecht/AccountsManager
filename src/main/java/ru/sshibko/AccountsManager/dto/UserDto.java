package ru.sshibko.AccountsManager.dto;

import lombok.*;
import ru.sshibko.AccountsManager.model.entity.Role;
import ru.sshibko.AccountsManager.model.entity.Account;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String login;
    private String password;
    private boolean status;
    private List<Account> accountList;
    private Role role;
}
