package com.software.grey.utils;

import com.software.grey.models.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class SecurityUtils {

    private SecurityUtils() {}

    public static UUID getCurrentUserId(){
        Optional<UUID> userIdOptional = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> authentication.getPrincipal() instanceof User)
                .map(authentication -> ((User) authentication.getPrincipal()).getId());
        return userIdOptional.orElseThrow(() -> new IllegalStateException("User ID not available"));
    }
}
