package com.software.grey.models.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BasicEntity{

    private String username;

    private String email;

    private String password;

    private boolean enabled;

    private boolean authenticated;

}