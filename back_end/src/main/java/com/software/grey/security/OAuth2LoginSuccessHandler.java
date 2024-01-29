package com.software.grey.security;

import com.software.grey.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${front.url}")
    private String frontUrl;

    private final UserService userService;

    public OAuth2LoginSuccessHandler(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        userService.saveGoogleUser((DefaultOAuth2User) authentication.getPrincipal());
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
