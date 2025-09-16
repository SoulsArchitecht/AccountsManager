package ru.sshibko.AccountsManager.mapper;

import org.springframework.stereotype.Component;
import ru.sshibko.AccountsManager.dto.UserDto;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.model.entity.Role;
import ru.sshibko.AccountsManager.model.entity.User;

import java.io.Serializable;

@Component
public class UserMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .password(user.getPassword())
                .status(user.isStatus())
                .role(user.getRole())
                .build();
    }

    public User toEntity(UserDto userDto) {
        Role role = null;
        if (userDto.getRole() != null) {
            try {
                role = userDto.getRole();
            } catch (IllegalArgumentException e) {
                throw new ResourceNotFoundException("Invalid role" + userDto.getRole());
            }
        }

        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .status(userDto.isStatus())
                .role(role)
                .build();
    }
}
