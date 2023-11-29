package com.software.grey.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_google_oauth")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@PrimaryKeyJoinColumn(name = "local_id")
public class GoogleUser extends User{

    @Column(unique = true, name = "external_id")
    private String externalID;
}
