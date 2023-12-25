package ru.sshibko.AccountsManager.service;

import ru.sshibko.AccountsManager.dto.UrlDto;

import java.util.List;

public interface UrlService {

    UrlDto createUrl(UrlDto urlDto);

    List<UrlDto> getAllUrls();

    UrlDto getUrlById(Long urlId);

    UrlDto updateUrl(Long urlId, UrlDto urlDto);

    void deleteUrl(Long urlId);
}
