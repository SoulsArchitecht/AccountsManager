package ru.sshibko.AccountsManager.service;

import ru.sshibko.AccountsManager.dto.AccountDto;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    List<AccountDto> getAllAccounts();

    AccountDto getAccountById(Long accountId);

    AccountDto updateAccount(Long accountId, AccountDto accountDto);

    void deleteAccount(Long accountId);
}
