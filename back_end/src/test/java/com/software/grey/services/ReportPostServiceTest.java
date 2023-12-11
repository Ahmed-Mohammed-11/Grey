package com.software.grey.services;

import com.software.grey.controllers.SignupController;
import com.software.grey.exceptions.exceptions.DataNotFoundException;
import com.software.grey.exceptions.exceptions.UserReportedPostBeforeException;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.ReportedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.ReportedPostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static com.software.grey.models.enums.Feeling.*;
import static com.software.grey.models.enums.Feeling.SAD;
import static java.lang.Math.ceil;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportPostServiceTest {

    @Autowired
    private ReportedPostRepository reportedPostRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepo userRepo;

    @MockBean
    private SecurityUtils securityUtils;

    @Autowired
    private PostService postService;

    @Autowired
    private SignupController signup;

    private String user1;
    private String user2;
    private ArrayList<UUID> posts;


    @BeforeAll
    void init() {
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
                            .postText("Some bad text" + i)
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
                    .postText("Some bad text" + i)
                    .postFeelings(Set.of(FEAR, ANGER))
                    .build()));
        for (int i = 0; i < 3; i++)
            posts.add(postService.add(PostDTO.builder()
                    .postText("Some bad text other than the first bad text" + i)
                    .postFeelings(Set.of(ANXIOUS, SAD))
                    .build()));
    }

    @AfterAll
    void cleanUp() {
        userRepo.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    void reportExistingPost_shouldBeValid(){
        User user = userRepo.findByUsername("mockedUserName1");
        when(securityUtils.getCurrentUser()).thenReturn(user);


        UUID postId = posts.get(0);
        Optional<Post> post = postRepository.findById(postId);

        postService.report(postId.toString());
        assertThat(reportedPostRepository.existsById(new ReportedPostId(post.get(), user))).isTrue();
    }

    @Test
    void duplicateReportExistingPost_shouldThrowException(){
        User user = userRepo.findByUsername("mockedUserName1");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        UUID postId = posts.get(3);
        Optional<Post> post = postRepository.findById(postId);

        postService.report(postId.toString());
        assertThat(reportedPostRepository.existsById(new ReportedPostId(post.get(), user))).isTrue();
        Assertions.assertThrows(UserReportedPostBeforeException.class,
                () -> postService.report(postId.toString()));
    }

    @Test
    void reportNotExistingPost_shouldThrowException(){
        UUID randomUUID = UUID.randomUUID();
        while (postRepository.existsById(randomUUID))
            randomUUID = UUID.randomUUID();
        UUID finalRandomUUID = randomUUID;

        User user = userRepo.findByUsername("mockedUserName1");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        DataNotFoundException exception = Assertions.assertThrows(DataNotFoundException.class,
                () -> postService.report(finalRandomUUID.toString()));
        assertEquals("Post not found", exception.getMessage());
    }
}
