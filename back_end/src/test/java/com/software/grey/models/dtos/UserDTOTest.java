package com.software.grey.models.dtos;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try
        {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        catch (ValidationException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

    }

    @Test
    void emailShouldExist() {
        UserDTO user = new UserDTO("", "test_user", "pass 444 great");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usernameShouldExist() {
        UserDTO user = new UserDTO("test@gmail.com", "", "pass 444 great");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void passwordShouldExist() {
        UserDTO user = new UserDTO("test@gmail.com", "test_user", "");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void emailShouldBeValid() {
        UserDTO user = new UserDTO("testgmail.com", "test_user", "pass 444 great");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usernameShouldBeValid() {
        UserDTO user = new UserDTO("test@gmail.com", "test", "pass 444 great");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void passwordShouldBeValid() {
        UserDTO user = new UserDTO("test@gmail.com", "test_user", "weakpass");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validCredentials() {
        UserDTO user = new UserDTO("test@gmail.com", "test_user", "pass 444 great");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}
