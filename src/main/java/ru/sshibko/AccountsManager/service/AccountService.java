package ru.sshibko.AccountsManager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.exception.UnauthorizedAccessException;
import ru.sshibko.AccountsManager.mapper.AccountMapper;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.model.entity.Role;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.AccountRepository;
import ru.sshibko.AccountsManager.model.repository.UserRepository;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountService implements CRUDService<AccountDto>{

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AccountMapper accountMapper;

    private final UserService userService;

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
        User currentUser = userService.getCurrentUser();
        log.info("Creating new account for user with ID: {} ", currentUser.getId());
//        accountDto.setId(1L);
        Account account = accountMapper.toEntity(accountDto);
/*        Long userId = accountDto.getUserId();
        User user =  userRepository.findById(userId).orElseThrow();
        account.setUser(user);*/
        account.setActive(true);
        account.setUser(currentUser);
        //account.setId(1L);
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
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getRole().equals(Role.ROLE_ADMIN) && !currentUser.getId().equals(account.getUser().getId())) {
            throw new UnauthorizedAccessException("You do not have permission to update this account");
        }
        account.setUser(currentUser);

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

        User currentUser = userService.getCurrentUser();
        if (currentUser != null && (currentUser.getId().equals(account.getUser().getId()) ||
                currentUser.getRole() == Role.ROLE_ADMIN)) {
                account.setActive(false);
        }
        //TODO resolve delete or deactivate
        //accountRepository.delete(account);
        log.info("Account with ID {} deleted successfully!", accountId);
    }

    public Collection<AccountDto> findByKeyword(String keyword) {
        log.info("Finding accounts by keyword {}", keyword);
        List<Account> accountList = accountRepository.findByKeyword(keyword);
        return accountList.stream().map(accountMapper::toDto)
                .toList();
    }

/*    public Page<AccountDto> findByCurrentUserAndKeyword(String keyword, Pageable pageable) {
        log.info("Finding accounts by current user and keyword {}", keyword);
        Page<Account> accountList = accountRepository.findByCurrentUserAndKeyword(
                userService.getCurrentUser().getId(), keyword, pageable);
            return (Page<AccountDto>) accountList.stream().map(accountMapper::toDto);
    }*/

    public Page<AccountDto> findByCurrentUserAndKeyword(String keyword, Pageable pageable) {
        log.info("Finding accounts by current user and keyword {}", keyword);
        Page<Account> accountList = accountRepository.findByCurrentUserAndKeyword(
                userService.getCurrentUser().getId(), keyword, pageable);

        List<AccountDto> accountDtos = accountList.getContent().stream()
                .map(accountMapper::toDto)
                .toList();

        return new PageImpl<>(accountDtos, pageable, accountList.getTotalElements());
    }

    public Page<AccountDto> getAllAccountCurrentUser(Pageable pageable) {
        log.info("Getting all accounts for current user");
        return accountRepository.findAllUserAccounts(userService.getCurrentUser().getId(), pageable)
                .map(accountMapper::toDto);
    }
}
