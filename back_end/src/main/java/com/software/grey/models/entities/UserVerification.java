package com.software.grey.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
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
