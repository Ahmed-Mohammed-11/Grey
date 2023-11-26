package com.software.grey.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2LoginController {
    @GetMapping("/api/login/success")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttribute("email");
    }}
