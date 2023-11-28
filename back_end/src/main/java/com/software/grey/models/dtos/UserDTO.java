package com.software.grey.models.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static com.software.grey.utils.RegularExpressions.EMAIL_REGEX;
import static com.software.grey.utils.RegularExpressions.USERNAME_REGEX;
import static com.software.grey.utils.RegularExpressions.PASSWORD_REGEX;

@AllArgsConstructor
@NoArgsConstructor
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
}
