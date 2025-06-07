package ru.sshibko.AccountsManager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.AccountsManager.dto.PagedDataDto;
import ru.sshibko.AccountsManager.dto.UserDto;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.service.UserService;

import java.util.Collection;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PagedDataDto<User> getAllAccountPaged(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<User> pagedData = userService.findAllUsersPaged(keyword, PageRequest.of(page, size));

        PagedDataDto<User> pagedDataDto = new PagedDataDto<>();
        pagedDataDto.setData(pagedData.getContent());
        pagedDataDto.setTotal(pagedData.getTotalPages());

        return pagedDataDto;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserDto updatedUserDto) {
        return userService.update(id, updatedUserDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable("id") Long userId) {
        userService.delete(userId);
    }

    @GetMapping("/search/{keyword}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<UserDto> getByKeyword(@PathVariable("keyword") String keyword) {
        return userService.findByKeyword(keyword);
    }
}
