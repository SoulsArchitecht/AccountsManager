package ru.sshibko.AccountsManager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.dto.PagedDataDto;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.service.AccountService;
import java.util.Collection;

@CrossOrigin("*")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public AccountDto getAccountById(@PathVariable("id") Long id) {
        return accountService.getById(id);
    }

    @GetMapping()
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
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @PutMapping("/{id}")
    public AccountDto updateAccount(@PathVariable("id") Long id, @RequestBody AccountDto updatedAccountDto) {
        return accountService.update(id, updatedAccountDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable("id") Long accountId) {
        accountService.delete(accountId);
    }

    @GetMapping("/search/{keyword}")
    public Collection<AccountDto> getByKeyword(@PathVariable("keyword") String keyword) {
        return accountService.findByKeyword(keyword);
    }
}