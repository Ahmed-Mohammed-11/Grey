package com.software.grey.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
