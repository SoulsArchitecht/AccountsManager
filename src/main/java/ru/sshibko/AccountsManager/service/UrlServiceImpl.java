package ru.sshibko.AccountsManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sshibko.AccountsManager.dto.UrlDto;
import ru.sshibko.AccountsManager.exception.ResourceNotFoundException;
import ru.sshibko.AccountsManager.mapper.UrlMapper;
import ru.sshibko.AccountsManager.model.entity.Url;
import ru.sshibko.AccountsManager.model.repository.UrlRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public List<UrlDto> getAllUrls() {
         List<Url> urlList = urlRepository.findAll();
         return urlList.stream().map((url) -> UrlMapper.mapToUrlDto(url))
                 .collect(Collectors.toList());
    }

    @Override
    public UrlDto createUrl(UrlDto urlDto) {
        Url url = UrlMapper.mapToUrl(urlDto);
        Url savedUrl = urlRepository.save(url);
        return UrlMapper.mapToUrlDto(savedUrl);
    }

    @Override
    public UrlDto getUrlById(Long urlId) {
        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Url with given id: " + urlId + " is not exists"));
        return UrlMapper.mapToUrlDto(url);
    }

    @Override
    public UrlDto updateUrl(Long urlId, UrlDto updatedUrl) {
        Url url = urlRepository.findById(urlId).orElseThrow(
                () -> new ResourceNotFoundException((
                        "Url with given id: " + urlId + "is not exists"))
        );

        url.setLink(updatedUrl.link());
        url.setDescription(updatedUrl.description());
        url.setChangeAt(updatedUrl.createdAt());
        url.setChangeAt(updatedUrl.changedAt());
        url.setEmail(updatedUrl.email());
        url.setEmailAnother(updatedUrl.emailAnother());
        url.setLogin(updatedUrl.login());
        url.setPassword(updatedUrl.password());
        url.setNickName(updatedUrl.nickname());
        url.setActive(updatedUrl.active());

        Url updated = urlRepository.save(url);
        return UrlMapper.mapToUrlDto(updated);
    }

    @Override
    public void deleteUrl(Long urlId) {
        Url url = urlRepository.findById(urlId).orElseThrow(
                () -> new ResourceNotFoundException((
                        "Url with given id: " + urlId + "is not exists"))
        );

        urlRepository.deleteById(urlId);
    }
}
