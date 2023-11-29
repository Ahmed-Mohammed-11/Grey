package com.software.grey.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.software.grey.utils.EndPoints.*;

@RestController
public class TestController {
    @GetMapping(TEST)
    public String testEndpoint() {
        return "Test success";
    }

    @GetMapping(LOGIN)
    public String loginEndpoint() {
        return "login";
    }

    @GetMapping(ROOT)
    public String welcomeGrey() {
        return "Welcome to grey!";
    }
}
