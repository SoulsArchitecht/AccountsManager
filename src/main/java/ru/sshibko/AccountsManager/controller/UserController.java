package ru.sshibko.AccountsManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "User Management API")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "get user for current user or ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @GetMapping()
    @Operation(summary = "get all user for ADMIN only")
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
    @Operation(summary = "Create a new user for ADMIN only")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user by ID for current USER or ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserDto updatedUserDto) {
        return userService.update(id, updatedUserDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove an existing user by ID for ADMIN only")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable("id") Long userId) {
        userService.delete(userId);
    }

    @GetMapping("/search/{keyword}")
    @Operation(summary = "Search users by keyword for ADMIN only")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<UserDto> getByKeyword(@PathVariable("keyword") String keyword) {
        return userService.findByKeyword(keyword);
    }
}
