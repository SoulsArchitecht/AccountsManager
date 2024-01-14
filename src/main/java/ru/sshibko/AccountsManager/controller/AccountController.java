package ru.sshibko.AccountsManager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.service.AccountService;

import java.util.Collection;

@CrossOrigin("*")
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @ResponseBody
    @GetMapping("/{id}")
    public AccountDto getAccountById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    @ResponseBody
    @GetMapping
    public Collection<AccountDto> getAllAccounts() {
        return accountService.getAll();
    }

    @ResponseBody
    @PostMapping
    public void createAccount(@RequestBody AccountDto accountDto) {
        accountService.create(accountDto);
    }

    @ResponseBody
    @PutMapping("/{id}")
    public void updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        //TODO Long id delete?
        accountService.update(accountDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.delete(id);
    }
}