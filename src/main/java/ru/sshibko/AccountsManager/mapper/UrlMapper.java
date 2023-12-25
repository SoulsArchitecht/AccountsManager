package ru.sshibko.AccountsManager.mapper;

import ru.sshibko.AccountsManager.dto.UrlDto;
import ru.sshibko.AccountsManager.model.entity.Url;

public class UrlMapper {

    public static UrlDto mapToUrlDto(Url url) {
        return new UrlDto(
                url.getId(),
                url.getLink(),
                url.getDescription(),
                url.getCreatedAt(),
                url.getChangeAt(),
                url.getLogin(),
                url.getEmail(),
                url.getEmailAnother(),
                url.getNickName(),
                url.getPassword(),
                url.isActive(),
                url.getUser()
        );
    }

    public static Url mapToUrl(UrlDto urlDto) {
        return new Url(
                urlDto.id(),
                urlDto.link(),
                urlDto.description(),
                urlDto.createdAt(),
                urlDto.changedAt(),
                urlDto.login(),
                urlDto.email(),
                urlDto.emailAnother(),
                urlDto.nickname(),
                urlDto.password(),
                urlDto.active(),
                urlDto.user()
        );
    }
}
