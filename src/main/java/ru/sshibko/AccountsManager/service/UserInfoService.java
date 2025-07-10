package ru.sshibko.AccountsManager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sshibko.AccountsManager.dto.UpdateUserInfoRequest;
import ru.sshibko.AccountsManager.dto.UserInfoDto;
import ru.sshibko.AccountsManager.mapper.UserInfoMapper;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.entity.UserInfo;
import ru.sshibko.AccountsManager.model.repository.UserInfoRepository;
import ru.sshibko.AccountsManager.model.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    private final FileStorageService fileStorageService;

    private final UserInfoMapper userInfoMapper;

    private final UserService userService;

    @Transactional
    public UserInfoDto getCurrentUserInfo() {
        User user = userService.getCurrentUser();
        UserInfo userInfo = userInfoRepository.findByUserId(user.getId());
        if (userInfo == null) {
            userInfo = createDefaultInfo(user);
        }
        return userInfoMapper.toDto(userInfo);
    }

    @Transactional
    public UserInfoDto updateCurrentUserInfo(UpdateUserInfoRequest updateRequest) {
        User user = userService.getCurrentUser();
        UserInfo userInfo = userInfoRepository.findByUserId(user.getId());

        userInfo.setFirstName(updateRequest.getFirstName());
        userInfo.setLastName(updateRequest.getLastName());
        userInfo.setBirthDate(updateRequest.getBirthDate());
        userInfo.setCountry(updateRequest.getCountry());
        userInfo.setPhoneNumber(updateRequest.getPhoneNumber());
        userInfo.setOptionalEmail(updateRequest.getOptionalEmail());
        userInfo.setAvatarUrl(userInfo.getAvatarUrl());

        UserInfo updatedUserInfo = userInfoRepository.save(userInfo);

        return userInfoMapper.toDto(updatedUserInfo);
    }

    public String uploadAvatar(MultipartFile file) {
        User user = userService.getCurrentUser();
        UserInfo userInfo = userInfoRepository.findByUserId(user.getId());
        if (userInfo == null) {
            userInfo = createDefaultInfo(user);
        }
        String filename = fileStorageService.store(file);
        userInfo.setAvatarUrl(filename);
        userInfoRepository.save(userInfo);

        return userInfo.getAvatarUrl();
    }

    private UserInfo createDefaultInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        return userInfoRepository.save(userInfo);
    }
}
