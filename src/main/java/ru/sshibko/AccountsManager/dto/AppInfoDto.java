package ru.sshibko.AccountsManager.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AppInfoDto {

    private String email;
    private String phone;
    private String telegram;
    private String versionNumber;
    private String versionStatus;
}
