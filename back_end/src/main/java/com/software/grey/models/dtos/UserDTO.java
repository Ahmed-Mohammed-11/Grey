package com.software.grey.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.software.grey.utils.RegularExpressions.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = EMAIL_REGEX, message = "Email Format isn't valid")
    public String email;

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = USERNAME_REGEX, message = "Username Format isn't valid")
    public String username;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = PASSWORD_REGEX, message = "Password Format isn't valid")
    public String password;

    public String externalID;

    public UserDTO(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
