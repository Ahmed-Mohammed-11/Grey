package com.software.grey.controllers;

import com.software.grey.models.enums.Role;
import com.software.grey.models.entities.User;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.repositories.UserRepo;
import com.software.grey.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SignupControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private SignupController signupController;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    void signupCorrectUser() {
        UserDTO myUser = new UserDTO("mockEmail@gmail.com", "mockUser", "mockPassword");
        signupController.signup(myUser);
        User user = userRepo.findByUsername("mockUser");
        assert(Objects.equals(user.getUsername(), "mockUser"));
        assert(Objects.equals(user.getEmail(), "mockEmail@gmail.com"));
        assertTrue(bCryptPasswordEncoder.matches("mockPassword", user.getPassword()));
        assert(user.getRole() == Role.ROLE_USER);
    }

    @Test
    void signUpDuplicateUsername() {
        UserDTO myUser = new UserDTO("mockEmail@gmail.com", "mockUser", "mockPassword");
        signupController.signup(myUser);
        myUser.email = "mockEmail2@gmail.com";
        ResponseEntity<String> responseEntity =  signupController.signup(myUser);
        assertTrue(responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    void signUpDuplicateEmail() {
        UserDTO myUser = new UserDTO("mockEmail@gmail.com", "mockUser", "mockPassword");
        signupController.signup(myUser);
        myUser.username = "notMockUser";
        ResponseEntity<String> responseEntity =  signupController.signup(myUser);
        assertTrue(responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST);
    }
}