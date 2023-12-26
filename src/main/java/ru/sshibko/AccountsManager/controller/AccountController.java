package ru.sshibko.AccountsManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.service.AccountService;
import ru.sshibko.AccountsManager.service.AccountServiceImpl;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountServiceImpl accountServiceImpl) {
        this.accountService = accountServiceImpl;
    }

    @GetMapping("/")
    public String viewHomePage() {
        return "home";
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accountDtoList = accountService.getAllAccounts();
        return ResponseEntity.ok(accountDtoList);
    }

    @PostMapping("/createAccount")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        AccountDto savedAccount = accountService.createAccount(accountDto);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @GetMapping("account/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long accountId) {
        AccountDto accountDto = accountService.getAccountById(accountId);
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable("id") Long accountId,
                                                @RequestBody AccountDto updatedAccount) {
        AccountDto accountDto = accountService.updateAccount(accountId, updatedAccount);
        return ResponseEntity.ok(accountDto);
    }

    @DeleteMapping("deleteAccount/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted successfully!");
    }
}
