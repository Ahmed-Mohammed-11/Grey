package com.software.grey.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserResponseDTO {

    public String username;

    public String email;

    public String role;
}
