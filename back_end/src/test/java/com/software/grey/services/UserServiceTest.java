package com.software.grey.services;

import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.User;
import com.software.grey.models.entities.UserVerification;
import com.software.grey.repositories.UserRepo;
import com.software.grey.repositories.UserVerificationRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;

    @Autowired
    UserVerificationRepo userVerificationRepo;
    @Test
    void userExistsShouldBeTrue() {
        UserDTO user = new UserDTO("aad12356hb@gmail.com", "aad24", "pass");
        userService.save(user);
        assertTrue(userService.userExists(user));
    }

    @Test
    void userExistsShouldBeFalse() {
        UserDTO user = new UserDTO("iDoNotExist@gmail.com", "accursedNotExists", "pass");
        assertFalse(userService.userExists(user));
    }

    @Test
    void registerUser_ShouldCreateUserAndVerificationCode() {
        // Given
        UserDTO userDTO = new UserDTO("newUser@gmail.com", "Newuser", "password123");

        // When
        userService.save(userDTO);

        // Then
        User registeredUser = userRepo.findByUsername(userDTO.username);
        assertNotNull(registeredUser, "User should be registered");
        assertNotNull(registeredUser.getId(), "User ID should be assigned");

        UserVerification userVerification = userVerificationRepo.findById(registeredUser.getId())
                .orElseThrow(() -> new RuntimeException("UserVerification not found"));
        assertNotNull(userVerification, "UserVerification should be created");
        assertNotNull(userVerification.getRegistrationConfirmationCode(), "Verification code should be generated");
    }

    @Test
    void verifyRegistration_ShouldSetEnabledField() {
        // Given
        UserDTO userDTO = new UserDTO("verifyMe@gmail.com", "Verifyme", "koala swiss grey");
        userService.save(userDTO);

        // When
        User user = userRepo.findByUsername(userDTO.username);
        UserVerification userVerification = userVerificationRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("UserVerification not found"));

        userService.verifyUser(user.getId(), userVerification.getRegistrationConfirmationCode());

        // Then
        User verifiedUser = userRepo.findByUsername(userDTO.username);
        assertTrue(verifiedUser.isEnabled(), "User should be enabled after verification");
    }

    @Test
    void verifyUser_WithInvalidCode_ShouldNotSetEnabledField() {
        // Given
        UserDTO userDTO = new UserDTO("verifyMe1@gmail.com", "Verifyme1", "koala swiss grey");
        userService.save(userDTO);
        User user = userRepo.findByUsername(userDTO.username);

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.verifyUser(user.getId(), "invalidCode");
        });

        User unverifiedUser = userRepo.findByUsername(userDTO.username);
        assertFalse(unverifiedUser.isEnabled(), "User should not be enabled with invalid verification code");
    }
}