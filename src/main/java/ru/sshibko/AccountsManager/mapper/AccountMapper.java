package ru.sshibko.AccountsManager.mapper;

import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.model.entity.Account;

import java.io.Serializable;

public class AccountMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    public static AccountDto mapToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setLink(account.getLink());
        accountDto.setDescription(account.getDescription());
        accountDto.setCreatedAt(account.getCreatedAt());
        accountDto.setChangedAt(account.getChangedAt());
        accountDto.setLogin(account.getLogin());
        accountDto.setEmail(account.getEmail());
        accountDto.setEmailAnother(accountDto.getEmailAnother());
        accountDto.setNickName(account.getNickName());
        accountDto.setPassword(account.getPassword());
        accountDto.setActive(account.isActive());
        accountDto.setUserId(account.getUser().getId());

        return accountDto;
    }

    public static Account mapToAccount(AccountDto accountDto) {
        Account account = new Account();

        account.setId(account.getId());
        account.setLink(accountDto.getLink());
        account.setDescription(accountDto.getDescription());
        account.setCreatedAt(accountDto.getCreatedAt());
        account.setChangedAt(accountDto.getChangedAt());
        account.setLogin(accountDto.getLogin());
        account.setEmail(accountDto.getEmail());
        account.setEmailAnother(accountDto.getEmailAnother());
        account.setNickName(accountDto.getNickName());
        account.setPassword(accountDto.getPassword());
        account.setActive(accountDto.isActive());

        return account;
    }


}
