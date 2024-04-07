package ru.sshibko.AccountsManager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.dto.PagedDataDto;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.service.AccountService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                accountService.getById(id)
        );
    }

/*    @GetMapping
    public ResponseEntity<Collection<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(
                accountService.getAll()
        );
    }*/

/*    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllAccountsPageable(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                accountService.findAllAccountsPaged(keyword, page, size)
        );
    }*/

    @GetMapping()
    public ResponseEntity<PagedDataDto<Account>> getAllAccountPaged(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Account> pagedData = accountService.findAllAccountsPaged(keyword, PageRequest.of(page, size));

        PagedDataDto<Account> pagedDataDto = new PagedDataDto<>();
        pagedDataDto.setData(pagedData.getContent());
        pagedDataDto.setTotal(pagedData.getTotalPages());

        return ResponseEntity.ok(
                pagedDataDto
        );
    }


    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        AccountDto newAccount = accountService.create(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newAccount);
        // return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable("id") Long id, @RequestBody AccountDto updatedAccountDto) {
        AccountDto accountDto = accountService.update(id, updatedAccountDto);
        return ResponseEntity.ok(accountDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long accountId) {
        accountService.delete(accountId);
        return ResponseEntity.ok("Account with id " + accountId + "deleted successfully!");
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Collection<AccountDto>> getByKeyword(@PathVariable("keyword") String keyword) {
        return ResponseEntity.ok(
                accountService.findByKeyword(keyword)
        );
    }
}