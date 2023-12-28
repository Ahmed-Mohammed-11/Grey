package com.software.grey.controllers;

import com.software.grey.exceptions.exceptions.DataNotFoundException;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.repositories.BasicUserRepo;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.repositories.UserVerificationRepo;
import com.software.grey.services.implementations.UserServiceImpl;
import com.software.grey.services.implementations.PostServiceImpl;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.software.grey.models.enums.Feeling.HAPPY;
import static com.software.grey.models.enums.Feeling.LOVE;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportPostControllerTest {

    private final SignupController signup;
    private final UserRepo userRepo;
    private final PostRepository postRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private final UserServiceImpl userService;
    @MockBean
    private final PostServiceImpl postService;
    @MockBean
    private final SecurityUtils securityUtils;
    @Autowired
    private BasicUserRepo basicUserRepo;
    @Autowired
    private UserVerificationRepo userVerificationRepo;

    private ArrayList<String> posts;

    @Autowired
    ReportPostControllerTest(UserServiceImpl userService, PostServiceImpl postService, SecurityUtils securityUtils,
                             SignupController signup, UserRepo userRepo, PostRepository postRepository) {
        this.userService = userService;
        this.postService = postService;
        this.securityUtils = securityUtils;
        this.signup = signup;
        this.userRepo = userRepo;
        this.postRepository = postRepository;
    }

    @BeforeAll
    void init() {
        postRepository.deleteAll();
        posts = new ArrayList<>();
        addUser();
    }

    void addUser() {
        when(securityUtils.getCurrentUserName()).thenReturn("grey User");
        UserDTO userDTO1 = new UserDTO("greyEmail@gmail.com", "grey User", "mock grey Pass");
        signup.signup(userDTO1);
        createPostsForUser();
    }

    private void createPostsForUser() {
        for (int i = 0; i < 5; i++)
            posts.add(postService
                    .add(PostDTO.builder()
                            .postText(i + " grey post")
                            .postFeelings(Set.of(LOVE, HAPPY))
                            .build()));
    }

    @AfterAll
    void cleanUpAll() {
        postRepository.deleteAll();
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void reportPost_shouldBeValid() throws Exception {
        when(securityUtils.getCurrentUser()).thenReturn(userRepo.findByUsername("grey User"));
        mockMvc.perform(MockMvcRequestBuilders.post(EndPoints.POST +
                                EndPoints.REPORT_POST + "/" + posts.get(0))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string("Post reported successfully!\n" +
                                "We will review your report and take the necessary actions."));
        verify(postService, times(1)).report(anyString());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void reportPost_shouldBeNotFound() throws Exception {
        UUID randomNotInPosts = UUID.randomUUID();
        while (posts.contains(randomNotInPosts))
            randomNotInPosts = UUID.randomUUID();

        when(securityUtils.getCurrentUser()).thenReturn(userRepo.findByUsername("grey User"));
        doThrow(new DataNotFoundException("Post not found"))
                .when(postService).report(randomNotInPosts.toString());

        String expectedErrorMessage = "{\"errorMessage\":\"Post not found\"}";
        mockMvc.perform(post(EndPoints.POST + EndPoints.REPORT_POST + "/" + randomNotInPosts)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string(expectedErrorMessage));
        verify(postService, times(1)).report(anyString());
    }

}