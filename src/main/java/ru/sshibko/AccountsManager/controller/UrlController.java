package ru.sshibko.AccountsManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.AccountsManager.dto.UrlDto;
import ru.sshibko.AccountsManager.service.UrlService;
import ru.sshibko.AccountsManager.service.UrlServiceImpl;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlServiceImpl urlServiceImpl) {
        this.urlService = urlServiceImpl;
    }

    @GetMapping("/")
    public String viewHomePage() {
        return "home";
    }

    @GetMapping("/urls")
    public ResponseEntity<List<UrlDto>> getAllUrls() {
        List<UrlDto> urlDtoList = urlService.getAllUrls();
        return ResponseEntity.ok(urlDtoList);
    }

    @PostMapping("/createUrl")
    public ResponseEntity<UrlDto> createUrl(@RequestBody UrlDto urlDto) {
        UrlDto savedUrl = urlService.createUrl(urlDto);
        return new ResponseEntity<>(savedUrl, HttpStatus.CREATED);
    }

    @GetMapping("url/{id}")
    public ResponseEntity<UrlDto> getUrlById(@PathVariable("id") Long urlId) {
        UrlDto urlDto = urlService.getUrlById(urlId);
        return ResponseEntity.ok(urlDto);
    }

    @PutMapping("/updateUrl/{id}")
    public ResponseEntity<UrlDto> updateUrl(@PathVariable("id") Long urlId,
                                            @RequestBody UrlDto updatedUrl) {
        UrlDto urlDto = urlService.updateUrl(urlId, updatedUrl);
        return ResponseEntity.ok(urlDto);
    }

    @DeleteMapping("deleteUrl/{id}")
    public ResponseEntity<String> deleteUrl(@PathVariable("id") Long urlId) {
        urlService.deleteUrl(urlId);
        return ResponseEntity.ok("Url deleted successfully!");
    }
}
