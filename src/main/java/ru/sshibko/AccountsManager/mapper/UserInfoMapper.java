package ru.sshibko.AccountsManager.mapper;

import org.springframework.stereotype.Component;
import ru.sshibko.AccountsManager.dto.UserInfoDto;
import ru.sshibko.AccountsManager.model.entity.UserInfo;

@Component
public class UserInfoMapper {

    public UserInfoDto toDto(UserInfo userInfo) {
        return UserInfoDto.builder()
                .id(userInfo.getId())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .birthDate(userInfo.getBirthDate())
                .country(userInfo.getCountry())
                .phoneNumber(userInfo.getPhoneNumber())
                .optionalEmail(userInfo.getOptionalEmail())
                .avatarUrl(userInfo.getAvatarUrl())
                .registrationDate(userInfo.getRegistrationDate())
                .build();
    }

    public UserInfo toEntity(UserInfoDto userInfoDto) {
        return UserInfo.builder()
                .id(userInfoDto.getId())
                .firstName(userInfoDto.getFirstName())
                .lastName(userInfoDto.getLastName())
                .birthDate(userInfoDto.getBirthDate())
                .country(userInfoDto.getCountry())
                .phoneNumber(userInfoDto.getPhoneNumber())
                .optionalEmail(userInfoDto.getOptionalEmail())
                .avatarUrl(userInfoDto.getAvatarUrl())
                .registrationDate(userInfoDto.getRegistrationDate())
                .build();
    }
}
