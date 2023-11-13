package com.software.grey.models.entities;

import com.software.grey.models.enums.Role;
import com.software.grey.models.enums.Tier;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @ManyToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private Avatar avatar;

    private boolean enabled;
    private boolean authenticated;

}
