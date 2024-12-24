package ru.sshibko.AccountsManager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                userService.getById(id)
        );
    }

    @GetMapping()
    public ResponseEntity<PagedDataDto<User>> getAllAccountPaged(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<User> pagedData = userService.findAllUsersPaged(keyword, PageRequest.of(page, size));

        PagedDataDto<User> pagedDataDto = new PagedDataDto<>();
        pagedDataDto.setData(pagedData.getContent());
        pagedDataDto.setTotal(pagedData.getTotalPages());

        return ResponseEntity.ok(
                pagedDataDto
        );
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto newUser = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newUser);
        // return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto updatedUserDto) {
        UserDto userDto = userService.update(id, updatedUserDto);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok("User with id " + userId + "deleted successfully!");
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Collection<UserDto>> getByKeyword(@PathVariable("keyword") String keyword) {
        return ResponseEntity.ok(
                userService.findByKeyword(keyword)
        );
    }
}
