package ru.sshibko.AccountsManager.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.mapper.AccountMapper;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.AccountRepository;
import ru.sshibko.AccountsManager.model.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountService implements CRUDService<AccountDto>{

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    @Override
    public AccountDto getById(Long id) {
        log.info("Account get by ID: " + id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account with given id: " + id + " is not exists"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public Collection<AccountDto> getAll() {
        log.info("Get all accounts");
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream().map(AccountMapper::mapToAccountDto)
                .toList();
    }

    @Override
    public void create(@Valid AccountDto accountDto) {
        log.info("Create new account");
        Account account = AccountMapper.mapToAccount(accountDto);
        Long userId = accountDto.getUserId();
        User user =  userRepository.findById(userId).orElseThrow();
        account.setUser(user);
        accountRepository.save(account);
    }

    @Override
    public void update(@Valid AccountDto accountDto) {
        log.info("Update account");
        Account account = AccountMapper.mapToAccount(accountDto);
        Long userId = accountDto.getUserId();
        User user =  userRepository.findById(userId).orElseThrow();
        account.setUser(user);
        accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete account with ID: " + id);
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException((
                        "Account with given id: " + id + "is not exists"))
        );

        accountRepository.deleteById(id);
    }
}
