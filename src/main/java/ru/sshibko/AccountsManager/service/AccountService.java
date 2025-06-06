package ru.sshibko.AccountsManager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.mapper.AccountMapper;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.AccountRepository;
import ru.sshibko.AccountsManager.model.repository.UserRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountService implements CRUDService<AccountDto>{

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AccountMapper accountMapper;

    @Override
    public AccountDto getById(Long id) {
        log.info("Account get by ID: {} ", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account with given id: " + id + " is not exists"));
        return accountMapper.toDto(account);
    }

    @Override
    public Collection<AccountDto> getAll() {
        log.info("Getting all accounts");
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream().map(accountMapper::toDto).toList();
    }

/*    public Map<String, Object> getAllAccountsPageable(String keyword, int page, int size) {
        List<Account> accountList = new ArrayList<>();
        Pageable pagination = PageRequest.of(page, size);
        Page<Account> accountPage;
        if (keyword == null) {
            accountPage = accountRepository.findAll(pagination);
        } else {
            accountPage = (Page<Account>) accountRepository.findByKeyword(keyword);
        }
        accountList = accountPage.getContent();

        Map<String, Object> response =
                new HashMap<String, Object>();
        response.put("accounts", accountList);
        response.put("totalPages", accountPage.getTotalPages());

        return response;
    }*/

//    public List<AccountDto> findAllPaged(PageRequest pageRequest) {
//        List<Account> accountList = accountRepository.findAllByPageRequest(pageRequest);
//        return accountList.stream().map(AccountMapper::mapToAccountDto)
//                .toList();
//    }

    public Page<Account> findAllAccountsPaged(String keyword, PageRequest pageRequest) {
        Page<Account> accountPage;
        if (keyword == null) {
            accountPage = accountRepository.findAll(pageRequest);
        } else {
            accountPage = accountRepository.findByKeywordPaged(keyword, pageRequest);
        }
        return accountPage;
    }


    @Override
    @Transactional
    public AccountDto create(@Valid AccountDto accountDto) {
        log.info("Creating new account");
        Account account = accountMapper.toEntity(accountDto);
        Long userId = accountDto.getUserId();
        User user =  userRepository.findById(userId).orElseThrow();
        account.setUser(user);
        Account savedAccount = accountRepository.save(account);
        log.info("Account with ID {} created successfully!", account.getId());
        return accountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto update(@Valid Long accountId, @Valid AccountDto updatedAccountDto) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account with given id " + accountId + " is not exists")
        );
        log.info("Updating account with ID {}", accountId);
        Long userId = updatedAccountDto.getUserId();
        User user =  userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with given id " + userId + " is not exists on" +
                        "account with id " + accountId)
        );
        account.setUser(user);

        if (updatedAccountDto.getDescription() != null) {
            account.setDescription(updatedAccountDto.getDescription());
        }
        if (updatedAccountDto.getEmail() != null) {
            account.setEmail(updatedAccountDto.getEmail());
        }
        if (updatedAccountDto.getEmailAnother() != null) {
            account.setEmailAnother(updatedAccountDto.getEmailAnother());
        }
        if (updatedAccountDto.getLink() != null) {
            account.setLink(updatedAccountDto.getLink());
        }
        if (updatedAccountDto.getLogin() != null) {
            account.setLogin(updatedAccountDto.getLogin());
        }
        if (updatedAccountDto.getPassword() != null) {
            account.setPassword(updatedAccountDto.getPassword());
        }
        if (updatedAccountDto.getNickName() != null) {
            account.setNickName(updatedAccountDto.getNickName());
        }

        Account updatedAccount = accountRepository.save(account);

        log.info("Account with ID {} updated successfully!", accountId);
        return accountMapper.toDto(updatedAccount);
    }

    @Override
    @Transactional
    public void delete(Long accountId) {
        log.info("Deleting account with ID {} ", accountId);
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException((
                        "Account with given id: " + accountId + "is not exists"))
        );

        accountRepository.delete(account);
        log.info("Account with ID {} deleted successfully!", accountId);
    }

    public Collection<AccountDto> findByKeyword(String keyword) {
        log.info("Finding accounts by keyword {}", keyword);
        List<Account> accountList = accountRepository.findByKeyword(keyword);
        return accountList.stream().map(accountMapper::toDto)
                .toList();
    }
}
