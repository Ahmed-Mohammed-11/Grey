package com.software.grey.utils;

import com.software.grey.models.entities.User;
import com.software.grey.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class SecurityUtils {

    private UserRepo userRepo;

    public String getCurrentUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser(){
        String username = getCurrentUserName();
        return userRepo.findByUsername(username);
    }
}