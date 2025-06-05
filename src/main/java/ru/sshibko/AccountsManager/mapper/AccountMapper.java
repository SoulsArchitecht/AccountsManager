package ru.sshibko.AccountsManager.mapper;

import lombok.RequiredArgsConstructor;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.UserRepository;

import java.io.Serializable;

@RequiredArgsConstructor
public class AccountMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UserRepository userRepository;

    public static AccountDto mapToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setLink(account.getLink());
        accountDto.setDescription(account.getDescription());
        accountDto.setCreatedAt(account.getCreatedAt());
        accountDto.setChangedAt(account.getChangedAt());
        accountDto.setLogin(account.getLogin());
        accountDto.setEmail(account.getEmail());
        accountDto.setEmailAnother(account.getEmailAnother());
        accountDto.setNickName(account.getNickName());
        accountDto.setPassword(account.getPassword());
        accountDto.setActive(account.isActive());
        accountDto.setUserId(account.getUser().getId());

        return accountDto;
    }

    public static Account mapToAccount(AccountDto accountDto) {
        Account account = new Account();
        //User user = userRepository.findById(accountDto.getUserId());

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
        //account.setUser(user);

        return account;
    }

    public static Account mapToAccount(Long id, AccountDto accountDto) {
        Account account = mapToAccount(accountDto);
        account.setId(id);

        return account;
    }


}
