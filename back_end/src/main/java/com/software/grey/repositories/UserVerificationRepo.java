package com.software.grey.repositories;

import com.software.grey.models.entities.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserVerificationRepo extends JpaRepository<UserVerification, UUID> {

    Optional<UserVerification> findById(String id);
}
