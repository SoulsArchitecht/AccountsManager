package ru.sshibko.AccountsManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Accounts", description = "Account Management API")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/")
    @Operation(summary = "Get current user accounts for current user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Page<AccountDto> getAllCurrentUserAccounts(Pageable pageable) {
        return accountService.getAllAccountCurrentUser(pageable);
    }

/*    @GetMapping()
    @Operation(summary = "Get account with keyword for current user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Page<AccountDto> getAllCurrentUserAccountsWithKeyword(
            @RequestParam(value = "keyword", required = false, defaultValue = "%") String keyword,
            Pageable pageable) {
        return accountService.findByCurrentUserAndKeyword(keyword, pageable);
    }*/

    @GetMapping()
    @Operation(summary = "Get accounts with keyword and active for current user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Page<AccountDto> getAllCurrentUserAccountsWithKeywordAndStatus(
            @RequestParam(value = "keyword", required = false, defaultValue = "%") String keyword,
            @RequestParam(value = "active", required = false, defaultValue = "true") Boolean active,
            Pageable pageable) {
        return accountService.findByCurrentUserWithKeywordAndStatus(keyword, active, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by id for account owner or ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public AccountDto getAccountById(@PathVariable("id") Long id) {
        return accountService.getById(id);
    }

/*    @GetMapping("/")
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
    }*/

    @PostMapping
    @Operation(summary = "Create a new account by current User or ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing account by id for USER account owner or ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public AccountDto updateAccount(@PathVariable("id") Long id, @RequestBody AccountDto updatedAccountDto) {
        return accountService.update(id, updatedAccountDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove existing account by ID for USER account owner or ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public void deleteAccount(@PathVariable("id") Long accountId) {
        accountService.delete(accountId);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = " Setting activate existing account with pointed id to opposite value")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public AccountDto activateToggle(@PathVariable("id") Long accountId) {
        return accountService.activateToggle(accountId);
    }

    @GetMapping("/search/{keyword}/")
    @Operation(summary = "search any account by link or description by entering keyword for ADMIN only")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<AccountDto> getByKeyword(@PathVariable("keyword") String keyword) {
        return accountService.findByKeyword(keyword);
    }

    @GetMapping("/search/{keyword}")
    @Operation(summary = "search current user accounts by link or description by entering keyword for USER and ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Page<AccountDto> getByKeyword(@PathVariable("keyword") String keyword, Pageable pageable) {
        return accountService.findByCurrentUserAndKeyword(keyword, pageable);
    }
}