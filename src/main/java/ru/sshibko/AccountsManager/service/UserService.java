package ru.sshibko.AccountsManager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.sshibko.AccountsManager.dto.UserDto;
import ru.sshibko.AccountsManager.exception.AccountAccessException;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.exception.UnauthorizedAccessException;
import ru.sshibko.AccountsManager.mapper.UserMapper;
import ru.sshibko.AccountsManager.model.entity.Account;
import ru.sshibko.AccountsManager.model.entity.Role;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.repository.UserRepository;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserService implements CRUDService<UserDto> {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto getById(Long id) {
        log.info("User getting by ID {} ", id);
        User currentUser = getCurrentUser();
        if (!isAdmin(currentUser) && !id.equals(currentUser.getId())) {
            throw new AccountAccessException( "You do not have permission to access this resource");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with given id: " + id + " is not exists"));
        return userMapper.toDto(user);
    }

    @Override
    public Collection<UserDto> getAll() {
        log.info("Getting all users");
        User currentUser = getCurrentUser();
        if (!isAdmin(currentUser)) {
            throw new AccountAccessException( "You do not have permission to access this resource");
        }
        List<User> userList = userRepository.findAll();
        return userList.stream().map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        log.info("Creating new user");
        User currentUser = getCurrentUser();
        if (!isAdmin(currentUser)) {
            throw new AccountAccessException( "You do not have permission to access this resource");
        }
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        log.info("Updating user with id {} ", id);
        User currentUser = getCurrentUser();
        if (!isAdmin(currentUser) && !id.equals(currentUser.getId())) {
            throw new AccountAccessException( "You do not have permission to access this resource");
        }
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User currentUser = getCurrentUser();
        log.info("Deleting account with ID {} ", id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException((
                        "User with given id: " + id + "is not exists"))
        );
        if (!isAdmin(currentUser) && !user.equals(currentUser)) {
            throw new AccountAccessException( "You do not have permission to access this resource");
        }
        userRepository.delete(user);
    }

    public Page<User> findAllUsersPaged(String keyword, PageRequest pageRequest) {
        Page<User> userPage;
        if (keyword == null) {
            userPage = userRepository.findAll(pageRequest);
        } else {
            userPage = userRepository.findUserByKeywordPaged(keyword, pageRequest);
        }
        return userPage;
    }

    public Collection<UserDto> findByKeyword(String keyword) {
        log.info("Finding users by keyword {}",  keyword);
        List<User> userList = userRepository.findUserByKeyword(keyword);
        return userList.stream().map(userMapper::toDto)
                .toList();
    }

    protected User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UnauthorizedAccessException("User not found with email: " + email));
    }

    protected boolean isAdmin(User currentUser) {
        return currentUser != null && currentUser.getRole().equals(Role.ROLE_ADMIN);
    }

    protected boolean isAccountOwner(Account account, User currentUser) {
        return currentUser != null && currentUser.getId().equals(account.getUser().getId());
    }

    //TODO filtering methods
}
