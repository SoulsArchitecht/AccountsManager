package ru.sshibko.AccountsManager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.sshibko.AccountsManager.dto.UserInfoDto;
import ru.sshibko.AccountsManager.mapper.UserInfoMapper;
import ru.sshibko.AccountsManager.model.entity.User;
import ru.sshibko.AccountsManager.model.entity.UserInfo;
import ru.sshibko.AccountsManager.model.repository.UserInfoRepository;
import ru.sshibko.AccountsManager.model.repository.UserRepository;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private UserInfoMapper userInfoMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserInfoService userInfoService;

    private User user;
    private UserInfo userInfo;
    private UserInfoDto userInfoDto;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).email("user@test.com").build();
        userInfo = UserInfo.builder().id(1L).user(user).build();
        userInfoDto = new UserInfoDto();

    }

    @Test
    @DisplayName("getCurrentUserInfo() should return dto if exists")
    void getCurrentUserInfoShouldReturnDtoIfExists() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(userInfoRepository.findByUserId(1L)).thenReturn(userInfo);
        when(userInfoMapper.toDto(userInfo)).thenReturn(userInfoDto);

        UserInfoDto result = userInfoService.getCurrentUserInfo();

        assertThat(result).isSameAs(userInfoDto);
    }

    @Test
    @DisplayName("getCurrentUserInfo() should create default if not exists")
    void getCurrentUserInfoShouldCreateDefaultIfNotExists() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(userInfoRepository.findByUserId(1L)).thenReturn(null);
        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(userInfo);
        when(userInfoMapper.toDto(userInfo)).thenReturn(userInfoDto);

        UserInfoDto result = userInfoService.getCurrentUserInfo();

        verify(userInfoRepository).save(any(UserInfo.class));
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("uploadAvatar() should store and update url of new file")
    void uploadAvatarShouldStoreAndUpdateUrlOfNewFile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        lenient().when(file.getOriginalFilename()).thenReturn("avatar.jpg");


        lenient().when(userService.getCurrentUser()).thenReturn(user);
        lenient().when(userInfoRepository.findByUserId(1L)).thenReturn(userInfo);
        lenient().when(userInfoRepository.save(userInfo)).thenReturn(userInfo);
        lenient().when(fileStorageService.store(file)).thenReturn("uploads/avatar.jpg");

        String result = userInfoService.uploadAvatar(file);

        assertThat(result).isEqualTo("uploads/avatar.jpg");
        verify(userInfoRepository).save(userInfo);
        assertThat(userInfo.getAvatarUrl()).isEqualTo("uploads/avatar.jpg");
    }
}