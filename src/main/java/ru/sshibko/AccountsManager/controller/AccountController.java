package ru.sshibko.AccountsManager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.dto.PagedDataDto;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.service.AccountService;
import java.util.Collection;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@CrossOrigin("*")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Page<AccountDto> getAllCurrentUserAccounts(Pageable pageable) {
        return accountService.getAllAccountCurrentUser(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AccountDto getAccountById(@PathVariable("id") Long id) {
        return accountService.getById(id);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PagedDataDto<Account> getAllAccountPaged(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Account> pagedData = accountService.findAllAccountsPaged(keyword, PageRequest.of(page, size));

        PagedDataDto<Account> pagedDataDto = new PagedDataDto<>();
        pagedDataDto.setData(pagedData.getContent());
        pagedDataDto.setTotal(pagedData.getTotalPages());

        return pagedDataDto;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public AccountDto updateAccount(@PathVariable("id") Long id, @RequestBody AccountDto updatedAccountDto) {
        return accountService.update(id, updatedAccountDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable("id") Long accountId) {
        accountService.delete(accountId);
    }

    @GetMapping("/search/{keyword}/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<AccountDto> getByKeyword(@PathVariable("keyword") String keyword) {
        return accountService.findByKeyword(keyword);
    }

    @GetMapping("/search/{keyword}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Page<AccountDto> getByKeyword(@PathVariable("keyword") String keyword, Pageable pageable) {
        return accountService.findByCurrentUserAndKeyword(keyword, pageable);
    }
}