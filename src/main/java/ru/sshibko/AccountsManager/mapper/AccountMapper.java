package ru.sshibko.AccountsManager.mapper;

import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.model.entity.Account;

public class AccountMapper {

    public static AccountDto mapToAccountDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getLink(),
                account.getDescription(),
                account.getCreatedAt(),
                account.getChangeAt(),
                account.getLogin(),
                account.getEmail(),
                account.getEmailAnother(),
                account.getNickName(),
                account.getPassword(),
                account.isActive(),
                account.getUser()
        );
    }

    public static Account mapToAccount(AccountDto accountDto) {
        return new Account(
                accountDto.id(),
                accountDto.link(),
                accountDto.description(),
                accountDto.createdAt(),
                accountDto.changedAt(),
                accountDto.login(),
                accountDto.email(),
                accountDto.emailAnother(),
                accountDto.nickname(),
                accountDto.password(),
                accountDto.active(),
                accountDto.user()
        );
    }
}
