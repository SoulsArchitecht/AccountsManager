package ru.sshibko.AccountsManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sshibko.AccountsManager.dto.UpdateUserInfoRequest;
import ru.sshibko.AccountsManager.dto.UserInfoDto;
import ru.sshibko.AccountsManager.service.UserInfoService;

@RestController
@RequestMapping("/users/info")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/me")
    @Operation(summary = "Getting user details for owner or ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserInfoDto getCurrentUserInfo() {
        return userInfoService.getCurrentUserInfo();
    }

    @PutMapping("/me")
    @Operation(summary = "Updating user details for owner only")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserInfoDto updateCurrentUserInfo(
            @Valid @RequestBody UpdateUserInfoRequest request) {
        return userInfoService.updateCurrentUserInfo(request);
    }

    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Uploading avatar for owner")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String uploadAvatar(
            @Parameter(description = "Avatar image file")
            @RequestParam("file") MultipartFile file) {
        return userInfoService.uploadAvatar(file);
    }
}
