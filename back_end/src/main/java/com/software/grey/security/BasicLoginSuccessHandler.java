package com.software.grey.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.software.grey.utils.EndPoints.LOGIN_SUCCESS;

@Component
public class BasicLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Value("${front.url}")
    private String frontUrl;


    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(LOGIN_SUCCESS);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
