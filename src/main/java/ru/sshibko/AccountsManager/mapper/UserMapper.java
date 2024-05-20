package ru.sshibko.AccountsManager.mapper;

import ru.sshibko.AccountsManager.dto.UserDto;
import ru.sshibko.AccountsManager.model.entity.User;

import java.io.Serializable;

public class UserMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setStatus(user.isStatus());
        userDto.setAccountList(user.getAccountList());
        userDto.setRoleList(user.getRoleList());

        return userDto;
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();

        user.setId(user.getId());
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setStatus(userDto.isStatus());
        user.setAccountList(userDto.getAccountList());
        user.setRoleList(userDto.getRoleList());

        return user;
    }
}
