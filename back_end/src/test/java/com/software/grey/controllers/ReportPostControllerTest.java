package com.software.grey.controllers;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.services.UserService;
import com.software.grey.services.implementations.PostService;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.software.grey.models.enums.Feeling.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class ReportPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PostService postService;

    @MockBean
    private SecurityUtils securityUtils;

    private final SignupController signup;
    private final UserRepo userRepo;
    private final PostRepository postRepository;
    private String user1;
    private String user2;
    private ArrayList<UUID> posts;

    @Autowired
    ReportPostControllerTest(UserService userService, PostService postService, SecurityUtils securityUtils,
                             SignupController signup, UserRepo userRepo, PostRepository postRepository) {
        this.userService = userService;
        this.postService = postService;
        this.securityUtils = securityUtils;
        this.signup = signup;
        this.userRepo = userRepo;
        this.postRepository = postRepository;
    }

    @BeforeAll
    void init() throws InterruptedException {
        postRepository.deleteAll();
        posts = new ArrayList<>();
        addUser1();
        addUser2();
    }

    void addUser1() {
        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName1");
        UserDTO userDTO1 = new UserDTO("mockEmail1@gmail.com", "mockedUserName1", "mockPas1");
        signup.signup(userDTO1);
        user1 = "mockedUserName1";
        cretePostsForUser1();
    }

    private void cretePostsForUser1() {
        for (int i = 0; i < 5; i++)
            posts.add(postService
                    .add(PostDTO.builder()
                            .postText(i + " user1")
                            .postFeelings(Set.of(LOVE, HAPPY))
                            .build()));
    }

    void addUser2() {
        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName2");
        UserDTO userDTO2 = new UserDTO("mockEmail2@gmail.com", "mockedUserName2", "mockPas2");
        signup.signup(userDTO2);
        user2 = user1 = "mockedUserName2";
        cretePostsForUser2();
    }

    private void cretePostsForUser2() {
        for (int i = 0; i < 3; i++)
            posts.add(postService.add(PostDTO.builder()
                            .postText(i + " user2")
                            .postFeelings(Set.of(FEAR, ANGER))
                            .build()));
        for (int i = 0; i < 3; i++)
            posts.add(postService.add(PostDTO.builder()
                    .postText(i + " user2")
                    .postFeelings(Set.of(ANXIOUS, SAD))
                    .build()));
    }

    @AfterAll
    void cleanUp() {
        userRepo.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void reportPost_shouldBeValid() throws Exception {
        when(securityUtils.getCurrentUser()).thenReturn(userRepo.findByUsername("mockedUserName1"));
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
        while(posts.contains(randomNotInPosts))
            randomNotInPosts = UUID.randomUUID();

        System.out.println("randomNotInPosts = " + randomNotInPosts);
        System.out.println("posts = " + posts);

        when(securityUtils.getCurrentUser()).thenReturn(userRepo.findByUsername("mockedUserName1"));
        mockMvc.perform(MockMvcRequestBuilders.post(EndPoints.POST +
                                EndPoints.REPORT_POST + "/" + "randomNotInPosts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string("Post Not Found"));
        verify(postService, times(1)).report(anyString());
    }
}