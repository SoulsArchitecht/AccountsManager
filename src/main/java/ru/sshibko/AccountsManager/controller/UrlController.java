package ru.sshibko.AccountsManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UrlController {

    @GetMapping("/")
    public String viewHomePage() {
        return "home";
    }
}
