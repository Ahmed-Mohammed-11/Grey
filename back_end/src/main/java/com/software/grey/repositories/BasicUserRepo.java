package com.software.grey.repositories;

import com.software.grey.models.entities.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BasicUserRepo extends JpaRepository<BasicUser, UUID> {
    public BasicUser findByUsername(String username);
    public BasicUser findByEmail(String email);
    public Boolean existsByUsername(String username);
    public Boolean existsByEmail(String email);
}
