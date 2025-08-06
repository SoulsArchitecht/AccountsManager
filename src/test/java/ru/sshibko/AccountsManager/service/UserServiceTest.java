package ru.sshibko.AccountsManager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.sshibko.AccountsManager.dto.UserDto;
import ru.sshibko.AccountsManager.exception.AccountAccessException;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.mapper.UserMapper;
import ru.sshibko.AccountsManager.model.entity.Role;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.UserRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User adminUser;
    private User regularUser;
    private User anotherUser;
    private UserDto adminUserDto;
    private UserDto regularUserDto;
    private UserDto anotherUserDto;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        adminUser = User.builder()
                .id(1L)
                .email("admin@test.com")
                .role(Role.ROLE_ADMIN)
                .status(true)
                .build();

        regularUser = User.builder()
                .id(2L)
                .email("user@test.com")
                .role(Role.ROLE_USER)
                .status(true)
                .build();

        anotherUser = User.builder()
                .id(3L)
                .email("another@test.com")
                .role(Role.ROLE_USER)
                .status(true)
                .build();

        adminUserDto = UserDto.builder()
                .id(1L)
                .email("admin@test.com")
                .role(Role.ROLE_ADMIN)
                .status(true)
                .build();

        regularUserDto = UserDto.builder()
                .id(2L)
                .email("user@test.com")
                .role(Role.ROLE_USER)
                .status(true)
                .build();

        anotherUserDto = UserDto.builder()
                .id(3L)
                .email("another@test.com")
                .role(Role.ROLE_USER)
                .status(true)
                .build();
    }

    private void authenticateUser(String email) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private UserService spyUserService() {
        UserService spy = Mockito.spy(userService);
        doReturn(adminUser).when(spy).getCurrentUser();
        return spy;
    }

    /* ──────────────────────────────────────────────────────────────────────────
   TEST: getById
   ────────────────────────────────────────────────────────────────────────── */
    @Test
    @DisplayName("getById() when user is ADMIN should return any user")
    void getByIdAdminShouldGetAnyUser() {
        authenticateUser("admin@test.com");
        when(userRepository.findUserByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(anotherUser));
        when(userMapper.toDto(anotherUser)).thenReturn(anotherUserDto);

        UserDto result = userService.getById(3L);

        assertThat(result.getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("getById() when user is regular and owns the account should return own data")
    void getByIdUserCanGetOwnData() {
        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(userMapper.toDto(regularUser)).thenReturn(regularUserDto);

        UserDto result = userService.getById(2L);

        assertThat(result.getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("getById() when user is regular and tries to get another user should throw exception")
    void getByIdUserCannotGetAnotherUser() {
        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));

        assertThatThrownBy(() -> userService.getById(3L))
                .isInstanceOf(AccountAccessException.class)
                .hasMessage("You do not have permission to access this resource");
    }

    @Test
    @DisplayName("getById() when user not found should throw ResourceNotFoundException")
    void getByIdUserNotFoundShouldThrowResourceNotFoundException() {
        authenticateUser("admin@test.com");
        when(userRepository.findUserByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with given id: 999 is not exists");
    }

        /* ──────────────────────────────────────────────────────────────────────────
       TEST: getAll
       ────────────────────────────────────────────────────────────────────────── */

    @Test
    @DisplayName("getAll() when user is ADMIN should return all users")
    void getAllShouldGetAllUsers() {
        authenticateUser("admin@test.com");
        when(userRepository.findUserByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(userRepository.findAll()).thenReturn(java.util.List.of(adminUser, regularUser, anotherUser));
        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            if (user.getId() == 2L) {
                return regularUserDto;
            } else if (user.getId() == 3L) {
                return anotherUserDto;
            }
            return adminUserDto;
        });

        var result = userService.getAll();

        assertThat(result).hasSize(3);
        //assertThat(result).containsExactlyInAnyOrder(adminUserDto, regularUserDto);
        assertThat(result).extracting("id").containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @DisplayName("getAll() when user is not ADMIN should throw exception")
    void getAllShouldThrowExceptionWhenUserIsNotAdmin() {
        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));

        assertThatThrownBy(() -> userService.getAll())
                .isInstanceOf(AccountAccessException.class)
                .hasMessage("You do not have permission to access this resource");
    }

        /* ──────────────────────────────────────────────────────────────────────────
       TEST: create
       ────────────────────────────────────────────────────────────────────────── */

    @Test
    @DisplayName("create() when user is ADMIN should create user")
    void createShouldCreateUserByAdmin() {
        UserDto newUserDto = UserDto.builder()
                .email("new@test.com")
                .login("newuser")
                .password("password123")
                .role(Role.ROLE_USER)
                .status(true)
                .build();

        User newUserEntity = User.builder()
                .id(4L)
                .email("new@test.com")
                .login("newuser")
                .password("password123")
                .role(Role.ROLE_USER)
                .status(true)
                .build();

        User savedUserEntity = newUserEntity.toBuilder().id(4L).build();
        UserDto savedUserDto = newUserDto.toBuilder().id(4L).build();

        authenticateUser("admin@test.com");
        when(userRepository.findUserByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(userMapper.toEntity(newUserDto)).thenReturn(newUserEntity);
        when(userRepository.save(newUserEntity)).thenReturn(savedUserEntity);
        when(userMapper.toDto(savedUserEntity)).thenReturn(savedUserDto);

        UserDto result = userService.create(newUserDto);

        assertThat(result.getId()).isEqualTo(4L);
        assertThat(result.getEmail()).isEqualTo("new@test.com");
        verify(userRepository).save(newUserEntity);
    }

    @Test
    @DisplayName("create() when user is not ADMIN should throw exception")
    void createShouldThrowExceptionWhenUserIsNotAdmin() {
        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));

        assertThatThrownBy(() -> userService.create(regularUserDto))
                .isInstanceOf(AccountAccessException.class)
                .hasMessage("You do not have permission to access this resource");
    }

        /* ──────────────────────────────────────────────────────────────────────────
       TEST: update
       ────────────────────────────────────────────────────────────────────────── */

    @Test
    @DisplayName("update() when user is ADMIN should update any user")
    void updateShouldUpdateAnyUserByAdmin() {
        UserDto updateDto = anotherUserDto.toBuilder().login("updatedLogin").build();
        User updatedEntity = anotherUser.toBuilder().login("updatedLogin").build();

        authenticateUser("admin@test.com");
        when(userRepository.findUserByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(userMapper.toEntity(updateDto)).thenReturn(updatedEntity);
        when(userRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(userMapper.toDto(updatedEntity)).thenReturn(updateDto);

        UserDto result = userService.update(3L, updateDto);

        assertThat(result.getLogin()).isEqualTo("updatedLogin");
        verify(userRepository).save(updatedEntity);
    }

    @Test
    @DisplayName("update() when user updates own data should succeed")
    void updateShouldUpdateOwnDataByUser() {
        UserDto updateDto = regularUserDto.toBuilder().login("updatedLogin").build();
        User updatedEntity = regularUser.toBuilder().login("updatedLogin").build();

        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));
        when(userMapper.toEntity(updateDto)).thenReturn(updatedEntity);
        when(userRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(userMapper.toDto(updatedEntity)).thenReturn(updateDto);

        UserDto result = userService.update(2L, updateDto);

        assertThat(result.getLogin()).isEqualTo("updatedLogin");
    }

    @Test
    @DisplayName("update() when user tries to update another user should throw exception")
    void updateShouldThrowExceptionWhenUserIsNotOwner() {
        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));

        assertThatThrownBy(() -> userService.update(3L, anotherUserDto))
                .isInstanceOf(AccountAccessException.class)
                .hasMessage("You do not have permission to access this resource");
    }

        /* ──────────────────────────────────────────────────────────────────────────
       TEST: delete
       ────────────────────────────────────────────────────────────────────────── */

    @Test
    @DisplayName("delete() when user is ADMIN should delete any user")
    void deleteShouldDeleteUserByAdmin() {
        authenticateUser("admin@test.com");
        when(userRepository.findUserByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(anotherUser));

        userService.delete(3L);

        verify(userRepository).delete(anotherUser);
    }

    @Test
    @DisplayName("delete() when user deletes own account should succeed")
    void deleteShouldDeleteOwnAccountByUser() {
        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));

        userService.delete(2L);

        verify(userRepository).delete(regularUser);
    }

    @Test
    @DisplayName("delete() when user tries to delete another user should throw exception")
    void deleteShouldThrowExceptionWhenUserIsNotOwner() {
        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(anotherUser));

        assertThatThrownBy(() -> userService.delete(3L))
                .isInstanceOf(AccountAccessException.class)
                .hasMessage("You do not have permission to access this resource");
    }

    @Test
    @DisplayName("delete() when user not found should throw ResourceNotFoundException")
    void deleteShouldThrowExceptionWhenUserNotFound() {
        authenticateUser("admin@test.com");
        when(userRepository.findUserByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with given id: 999is not exists");
    }

        /* ──────────────────────────────────────────────────────────────────────────
       TEST: statusToggle
       ────────────────────────────────────────────────────────────────────────── */

    @Test
    @DisplayName("statusToggle() when user is ADMIN should toggle any user status")
    void statusToggleShouldToggleUserStatusByAdmin() {
        User deactivatedUser = anotherUser.toBuilder().status(false).build();
        UserDto toggledDto = anotherUserDto.toBuilder().status(false).build();

        authenticateUser("admin@test.com");
        when(userRepository.findUserByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(anotherUser));
        when(userRepository.save(anotherUser)).thenReturn(deactivatedUser);
        when(userMapper.toDto(deactivatedUser)).thenReturn(toggledDto);

        UserDto result = userService.statusToggle(3L);

        verify(userRepository).save(anotherUser);
        assertThat(result.isStatus()).isFalse();
    }

    @Test
    @DisplayName("statusToggle() when user is not ADMIN should throw exception")
    void statusToggleShouldThrowExceptionWhenUserIsNotAdmin() {
        authenticateUser("user@test.com");
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(Optional.of(regularUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));

        assertThatThrownBy(() -> userService.statusToggle(2L))
                .isInstanceOf(AccountAccessException.class)
                .hasMessage("You do not have permission to access this resource");
    }
}