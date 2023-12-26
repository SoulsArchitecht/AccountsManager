package ru.sshibko.AccountsManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.mapper.AccountMapper;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.model.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDto> getAllAccounts() {
         List<Account> accountList = accountRepository.findAll();
         return accountList.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                 .collect(Collectors.toList());
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account with given id: " + accountId + " is not exists"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto updateAccount(Long accountId, AccountDto updatedAccount) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException((
                        "Account with given id: " + accountId + "is not exists"))
        );

        account.setLink(updatedAccount.link());
        account.setDescription(updatedAccount.description());
        account.setChangeAt(updatedAccount.createdAt());
        account.setChangeAt(updatedAccount.changedAt());
        account.setEmail(updatedAccount.email());
        account.setEmailAnother(updatedAccount.emailAnother());
        account.setLogin(updatedAccount.login());
        account.setPassword(updatedAccount.password());
        account.setNickName(updatedAccount.nickname());
        account.setActive(updatedAccount.active());

        Account updated = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(updated);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException((
                        "Account with given id: " + accountId + "is not exists"))
        );

        accountRepository.deleteById(accountId);
    }
}
