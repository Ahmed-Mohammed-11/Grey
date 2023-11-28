package com.software.grey;

import com.software.grey.controllers.SignupController;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.software.grey.utils.EndPoints.ROOT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    void testUserLogin_ShouldSucceed() throws Exception {
        // Register a new user

        String username = "testusershouldsucceed";
        String password = "pass";
        UserDTO userDTO = new UserDTO("testusershouldsucceed`@gmail.com", username, password);
        signupController.signup(userDTO);

        // Attempt login with the registered user
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", username)
                        .param("password", password))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // Expect a redirect
                // .andExpect(redirectedUrl(ROOT));
    }

    @Test
    @DisplayName("Test login with incorrect password")
    void testLoginWithIncorrectPassword() throws Exception {
        // Register a new user
        String username = "testuserbadpass12345";
        String password = "pass";
        UserDTO userDTO = new UserDTO("testloginbadpass@gmail.com", username, password);
        signupController.signup(userDTO);

        // Attempt login with incorrect password
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", username)
                        .param("password", "incorrectPassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("Test login with non-existing user")
    void testLoginWithNonExistingUser() throws Exception {
        // Attempt login with a non-existing user
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "nonExistingUser")
                        .param("password", "somePassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // Expect a redirect
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("Test user logout")
    void testUserLogout_ShouldSucceed() throws Exception {
        String username = "t";
        String password = "pass";
        UserDTO userDTO = new UserDTO("t@gmail.com", username, password);
        signupController.signup(userDTO);
        // Log in a user
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                // .andExpect(redirectedUrl(ROOT));

        // Perform logout
        mockMvc.perform(MockMvcRequestBuilders.post("/logout"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

}
