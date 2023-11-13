package com.software.grey.repositories;

import com.software.grey.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    public User findByUsername(String username);
    public User findByEmail(String email);
}
