package ru.sshibko.AccountsManager.mapper;

import ru.sshibko.AccountsManager.dto.RoleDto;
import ru.sshibko.AccountsManager.model.entity.Role;

import java.io.Serializable;

public class RoleMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    public static RoleDto mapToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();

        roleDto.setId(role.getId());
        roleDto.setName(role.getName());

        return roleDto;
    }

    public static Role mapToRole(RoleDto roleDto) {
        Role role = new Role();

        role.setId(role.getId());
        role.setName(roleDto.getName());

        return role;
    }
}
