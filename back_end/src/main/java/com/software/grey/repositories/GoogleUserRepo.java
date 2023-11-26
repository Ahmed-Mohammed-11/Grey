package com.software.grey.repositories;

import com.software.grey.models.entities.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface GoogleUserRepo extends JpaRepository<GoogleUser, UUID> {
    public GoogleUser findByUsername(String username);
    public GoogleUser findByEmail(String email);
    public Boolean existsByUsername(String username);
    public Boolean existsByEmail(String email);
}

