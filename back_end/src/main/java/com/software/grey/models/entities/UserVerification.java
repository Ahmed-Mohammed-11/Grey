package com.software.grey.models.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "user_verification")
public class UserVerification {
    @Id
    private String id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private BasicUser user;

    private String registrationConfirmationCode;
}
