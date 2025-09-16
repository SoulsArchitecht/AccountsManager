package ru.sshibko.AccountsManager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.sshibko.AccountsManager.dto.security.AuthResponse;
import ru.sshibko.AccountsManager.dto.security.LoginRequest;
import ru.sshibko.AccountsManager.dto.security.RegisterRequest;
import ru.sshibko.AccountsManager.exception.EmailAlreadyExistsException;
import ru.sshibko.AccountsManager.model.entity.Role;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.UserRepository;
import ru.sshibko.AccountsManager.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(@Valid RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .login(request.getLogin())
                .role(Role.ROLE_USER)
                .status(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered with ID: {}", savedUser.getId());

        return authenticateUser(request.getEmail(), request.getPassword());
    }

    private AuthResponse authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        return new AuthResponse(jwt);
    }

/*    private AuthResponse authenticateUser(@NotBlank(message = "Email cannot be null")
                                          @Email(message = "email must be in correct form") String email,
                                          @NotBlank(message = "Password cannot be null")
                                          @Size(min = 8, message = "Password minimum length is 8") String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        return new AuthResponse(jwt);
    }*/

    public AuthResponse login(@Valid LoginRequest request) {
        return authenticateUser(request.getEmail(), request.getPassword());
    }
}
