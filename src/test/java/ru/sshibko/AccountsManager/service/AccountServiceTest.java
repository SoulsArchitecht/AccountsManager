package ru.sshibko.AccountsManager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.sshibko.AccountsManager.dto.AccountDto;
import ru.sshibko.AccountsManager.exception.AccountAccessException;
import ru.sshibko.AccountsManager.mapper.AccountMapper;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.model.entity.Role;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.AccountRepository;
import ru.sshibko.AccountsManager.model.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountService accountService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    private Account account;

    private AccountDto accountDto;

    private User currentUser;

    @BeforeEach
    void setUp() {
        currentUser = User.builder().id(1L).email("user@test.com").role(Role.ROLE_USER).build();

        account = Account.builder()
                .id(1L)
                .link("https://example.com")
                .login("testLogin")
                .password("encryptedPass")
                .email("test@example.com")
                .active(true)
                .user(currentUser)
                .build();

        accountDto = AccountDto.builder()
                .id(1L)
                .link("https://example.com")
                .login("testLogin")
                .password("newPass")
                .email("test@example.com")
                .userId(1L)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("getById() should return AccountDto")
    void getByIdWithValidIdShouldReturnAccountDto() {
        //Given
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);
        when(userService.isAccountOwner(account, currentUser)).thenReturn(true);

        //When
        AccountDto result = accountService.getById(1L);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(accountRepository).findById(1L);
    }

    @Test
    @DisplayName("getById() with no permissions should throw AccountAccessException")
    void getByIdWithNoPermissionsShouldThrowAccountAccessException() {
        User anotherUser = User.builder().id(2L).build();
        Account anotherAccount = Account.builder().user(anotherUser).build();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(anotherAccount));
        when(userService.isAccountOwner(anotherAccount, currentUser)).thenReturn(false);
        when(userService.isAdmin(currentUser)).thenReturn(false);

        assertThatThrownBy(() -> accountService.getById(1L))
                .isInstanceOf(AccountAccessException.class)
                .hasMessage("You do not have permission to access this resource");
    }

    @Test
    @DisplayName("create() with valid Dto should return AccountDto and create Account")
    void createWithValidDtoShouldReturnAccountDtoAndCreateAccount() {
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(accountMapper.toEntity(accountDto)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        AccountDto result = accountService.create(accountDto);

        assertThat(result).isNotNull();
        verify(accountRepository).save(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getUser()).isEqualTo(currentUser);
        assertThat(accountCaptor.getValue().isActive()).isTrue();
    }

    @Test
    @DisplayName("update() with valid data should update fields correctly")
    void updateWithValidDataShouldReturnAccountDtoAndUpdateAccount() {
        Account updatedAccountEntity = Account.builder().description("Updated description").build();
        AccountDto updatedAccountDto = AccountDto.builder().description("Updated description").build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(userService.isAccountOwner(account, currentUser)).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccountEntity);
        when(accountMapper.toDto(updatedAccountEntity)).thenReturn(updatedAccountDto);

        AccountDto result = accountService.update(1L, updatedAccountDto);

        assertThat(result.getDescription()).isEqualTo("Updated description");
        verify(accountRepository).save(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getDescription()).isEqualTo("Updated description");
    }

    @Test
    @DisplayName("activateToggle should toggle Active status")
    void activateToggleShouldToggleActiveStatus() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(userService.isAccountOwner(account, currentUser)).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDto);

        AccountDto result = accountService.activateToggle(1L);

        assertThat(result).isNotNull();
        assertThat(result.isActive()).isTrue();
    }

    @Test
    @DisplayName("delete() valid id should delete account")
    void deleteByIdWithValidIdShouldDeleteAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(userService.isAccountOwner(account, currentUser)).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(currentUser);

        accountService.delete(1L);

        verify(accountRepository).delete(account);
    }

    @Test
    @DisplayName("findByKeyword() should return mapped results")
    void findByKeywordShouldReturnMappedResults() {
        when(accountRepository.findByKeyword("test")).thenReturn(List.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        Collection<AccountDto> result = accountService.findByKeyword("test");

        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getAllAccountCurrentUser() should return page of Dtos")
    void getAllAccountCurrentUserShouldReturnDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Account> page = new PageImpl<>(List.of(account), pageable, 1);
        Page<AccountDto> dtoPage = new PageImpl<>(List.of(accountDto), pageable, 1);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(accountRepository.findAllUserAccounts(1L, pageable)).thenReturn(page);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        Page<AccountDto> result = accountService.getAllAccountCurrentUser(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}