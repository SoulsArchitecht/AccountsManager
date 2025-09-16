package ru.sshibko.AccountsManager.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.UserRepository;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class AccountMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UserRepository userRepository;

    public AccountDto toDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .link(account.getLink())
                .description(account.getDescription())
                .createdAt(account.getCreatedAt())
                .changedAt(account.getChangedAt())
                .login(account.getLogin())
                .email(account.getEmail())
                .emailAnother(account.getEmailAnother())
                .nickName(account.getNickName())
                .password(account.getPassword())
                .active(account.isActive())
                .userId(account.getUser().getId())
                .build();
    }

    public Account toEntity(AccountDto accountDto) {
/*        User user = userRepository.findById(accountDto.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User not exist with ID " + accountDto.getUserId())
        );*/
        return Account.builder()
                .link(accountDto.getLink())
                .description(accountDto.getDescription())
                .createdAt(accountDto.getCreatedAt())
                .changedAt(accountDto.getChangedAt())
                .login(accountDto.getLogin())
                .email(accountDto.getEmail())
                .emailAnother(accountDto.getEmailAnother())
                .nickName(accountDto.getNickName())
                .password(accountDto.getPassword())
                .active(accountDto.isActive())
                .build();
    }
}
