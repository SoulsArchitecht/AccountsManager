package ru.sshibko.AccountsManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
}
