package com.software.grey.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("test")
    public String testEndpoint(){
        return "Test success";
    }

    @GetMapping("login")
    public String loginEndpoint(){
        return "login";
    }

    @GetMapping("/")
    public String welcomeGrey(){
        return "Welcome to grey!";
    }
}
