package com.software.grey.controllers;

import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.BasicUser;
import com.software.grey.repositories.*;
import com.software.grey.utils.EndPoints;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.software.grey.utils.JsonUtil.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SecurityUtils securityUtils;
    private final SignupController signupController;
    private final PostRepository postRepository;
    private final UserRepo userRepo;
    private final UserVerificationRepo userVerificationRepo;
    private final GoogleUserRepo googleUserRepo;
    private final BasicUserRepo basicUserRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private String userId;

    @Autowired
    public UserControllerTest(SignupController signupController, PostRepository postRepository, UserRepo userRepo,
                              UserVerificationRepo userVerificationRepo, GoogleUserRepo googleUserRepo,
                              BasicUserRepo basicUserRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.signupController = signupController;
        this.postRepository = postRepository;
        this.userRepo = userRepo;
        this.userVerificationRepo = userVerificationRepo;
        this.googleUserRepo = googleUserRepo;
        this.basicUserRepo = basicUserRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @BeforeAll
    void init() {
        userVerificationRepo.deleteAll();
        googleUserRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepo.deleteAll();
        postRepository.deleteAll();
        UserDTO myUser = new UserDTO("mockemail@gmail.com", "old_username",
                "abc def ghi");
        signupController.signup(myUser);
        userId = userRepo.findByUsername(myUser.username).getId();
    }

    @AfterAll
    void cleanUp() {
        userVerificationRepo.deleteAll();
        googleUserRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepo.deleteAll();
    }
    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void testUpdateUserName() throws Exception{
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newUsername = "new_username";

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), newUsername, "abc def ghi");

        mockMvc.perform(
                MockMvcRequestBuilders.put(EndPoints.USER + EndPoints.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO))
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().string("Updated successfully")
        );

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(newUser.getUsername()).isEqualTo(newUsername);
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void testUpdateEmail() throws Exception {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newEmail = "newcontrolleremail@gmail.com";

        UserDTO userDTO = new UserDTO(newEmail, oldUser.getUsername(), "abc def ghi");

        mockMvc.perform(
                MockMvcRequestBuilders.put(EndPoints.USER + EndPoints.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO))
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().string("Updated successfully")
        );

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(newEmail);
    }


    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void testUpdatePassword() throws Exception {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newPassword = "new Password 123";

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), oldUser.getUsername(), newPassword);

        mockMvc.perform(
                MockMvcRequestBuilders.put(EndPoints.USER + EndPoints.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO))
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().string("Updated successfully")
        );

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(bCryptPasswordEncoder.matches(newPassword, newUser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void testInvalidUsernameFormat() throws Exception {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newUsername = "new username";

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), newUsername, "abc def ghi");

        mockMvc.perform(
                MockMvcRequestBuilders.put(EndPoints.USER + EndPoints.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.content().json("""
                        {
                            errorMessage: "Username Format isn't valid"
                        }
                        """
                )
        );

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void testInvalidEmailFormat() throws Exception {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newEmail = "invalid Email";

        UserDTO userDTO = new UserDTO(newEmail, oldUser.getUsername(), "abc def ghi");

        mockMvc.perform(
                MockMvcRequestBuilders.put(EndPoints.USER + EndPoints.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.content().json("""
                        {
                            errorMessage: "Email Format isn't valid"
                        }
                        """
                )
        );

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void testInvalidPasswordFormat() throws Exception {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newPassword = "invalidPassword";

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), oldUser.getUsername(), newPassword);

        mockMvc.perform(
                MockMvcRequestBuilders.put(EndPoints.USER + EndPoints.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.content().json("""
                        {
                            errorMessage: "Password Format isn't valid"
                        }
                        """
                )
        );

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(newUser.getPassword()).isEqualTo(oldUser.getPassword());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void testNullInput() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put(EndPoints.USER + EndPoints.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null))
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void testDuplicateUsername() throws Exception {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        UserDTO duplicate = new UserDTO("gg@gmail.com", "duplicateusername", "111 222 333");
        signupController.signup(duplicate);

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newUsername = "duplicateusername";

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), newUsername, "abc def ghi");

        mockMvc.perform(
                MockMvcRequestBuilders.put(EndPoints.USER + EndPoints.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.content().string("Something went wrong")
        );

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
    }
}
