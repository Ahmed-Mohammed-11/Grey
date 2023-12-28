package com.software.grey.repositories;

import com.software.grey.models.entities.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicUserRepo extends JpaRepository<BasicUser, String> {

    BasicUser findByUsername(String username);

    BasicUser findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
