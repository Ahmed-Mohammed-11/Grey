package com.software.grey.repositories;

import com.software.grey.models.entities.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GoogleUserRepo extends JpaRepository<GoogleUser, String> {

    GoogleUser findByUsername(String username);

    GoogleUser findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}

