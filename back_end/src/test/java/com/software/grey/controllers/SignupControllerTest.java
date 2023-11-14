package com.software.grey.controllers;

import com.software.grey.models.enums.Role;
import com.software.grey.models.entities.User;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.repositories.UserRepo;
import com.software.grey.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static com.software.grey.utils.EndPoints.SIGNUP;
import static com.software.grey.utils.JsonUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SignupControllerTest {
    @Autowired
    MockMvc mockMvc;
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

    @Test
    void signupWithNonValidEmail_ShouldFail() throws Exception {
        UserDTO myUser = new UserDTO("mockEmail", "mockUser", "mockPassword");
        mockMvc.perform(post(SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(myUser)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email form not valid"));
    }

    @Test
    void signupWithNonValidEmail2_ShouldFail() throws Exception {
        UserDTO myUser = new UserDTO("_mockE@m_ail@gmail.com", "mockUser", "mockPassword");
        mockMvc.perform(post(SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(myUser)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email form not valid"));
    }

    @Test
    void signupWithEmptyEmail_ShouldFail() throws Exception {
        UserDTO myUser = new UserDTO("", "mockUser", "mockPassword");
        mockMvc.perform(post(SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(myUser)))
                .andExpect(status().isBadRequest())
    }

    @Test
    void signupWithEmptyUsername_ShouldFail() throws Exception {
        UserDTO myUser = new UserDTO("valid@gmail.com", "", "mockPassword");
        mockMvc.perform(post(SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(myUser)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Username is mandatory"));
    }
}