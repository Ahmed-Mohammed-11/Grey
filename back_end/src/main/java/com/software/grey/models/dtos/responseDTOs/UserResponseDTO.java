package com.software.grey.models.dtos.responseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserResponseDTO {
    public String Username;
    public String email;
    public String role;
}
