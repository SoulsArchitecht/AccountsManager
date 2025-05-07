package ru.sshibko.AccountsManager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.sshibko.AccountsManager.dto.UserDto;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.mapper.UserMapper;
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

    @Override
    public UserDto getById(Long id) {
        log.info("User get by ID {} ", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with given id: " + id + " is not exists"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<UserDto> getAll() {
        log.info("Get all users");
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        log.info("Creating new user");
        User user = UserMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        log.info("Updating user with id {} ", id);
        User user = UserMapper.mapToUser(userDto);
        userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting account with ID {} ", id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException((
                        "User with given id: " + id + "is not exists"))
        );
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
        return userList.stream().map(UserMapper::mapToUserDto)
                .toList();
    }

    //TODO filtering methods

/*    public List<Role> getUserRoles(Long id) {
        log.info("Get all user roles");
        List<Role> roleList = repository.findAllByU
    }

    public List<Account> getUserAccounts(Long id) {
        log.info("Get all user accounts");
        List<Account> accountList = repository.findBy()
    }*/
}
