package ru.sshibko.AccountsManager.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sshibko.AccountsManager.dto.security.AuthResponse;
import ru.sshibko.AccountsManager.dto.security.LoginRequest;
import ru.sshibko.AccountsManager.dto.security.RegisterRequest;
import ru.sshibko.AccountsManager.exception.EmailAlreadyExistsException;
import ru.sshibko.AccountsManager.model.entity.Role;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.UserRepository;
import ru.sshibko.AccountsManager.security.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register() new user should create and return token")
    void registerNewUserShouldCreateAndReturnToken() {
        RegisterRequest request = RegisterRequest.builder()
                .email("new@user.com")
                .password("password123")
                .login("newuser")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .email("new@user.com")
                .password("$2a$10$...")
                .login("newuser")
                .role(Role.ROLE_USER)
                .status(true)
                .build();

        when(userRepository.existsByEmail("new@user.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$...");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenProvider.generateJwtToken(any())).thenReturn("mock-jwt-token");

        AuthResponse authResponse = authService.register(request);

        assertThat(authResponse.getToken()).isNotBlank();
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register() with exists email should throw EmailAlreadyExistsException")
    void registerExistingUserShouldThrowEmailAlreadyExistsException() {
        RegisterRequest request = RegisterRequest.builder().email("exists@user.com").build();
        when(userRepository.existsByEmail("exists@user.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email already exists");
    }

    @Test
    @DisplayName("login() with valid credentials should return token")
    void loginShouldReturnToken() {
        LoginRequest request = new LoginRequest("user@test.com", "password123");
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(jwtTokenProvider.generateJwtToken(auth)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
    }

}