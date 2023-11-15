package com.software.grey;

import com.software.grey.controllers.SignupController;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService; // Assuming you have a UserService

    @Autowired
    private SignupController signupController;

    @Test
    void testUserRegistrationAndLogin() throws Exception {
        // Register a new user
        String username = "testuser";
        String password = "pass";
        UserDTO userDTO = new UserDTO("test@gmail.com", username, password);
        signupController.signup(userDTO);

        // Attempt login with the registered user
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", username)
                        .param("password", password))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // Expect a redirect
                .andExpect(redirectedUrl("/"));
    }

}
