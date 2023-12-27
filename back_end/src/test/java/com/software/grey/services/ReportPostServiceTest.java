package com.software.grey.services;

import com.software.grey.controllers.SignupController;
import com.software.grey.exceptions.exceptions.PostNotFoundException;
import com.software.grey.exceptions.exceptions.UserReportedPostBeforeException;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.ReportedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.*;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.software.grey.models.enums.Feeling.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private UserService userService;

    @Autowired
    private SignupController signup;

    private String user1;
    private String user2;
    private ArrayList<Post> posts;
    @Autowired
    private BasicUserRepo basicUserRepo;
    @Autowired
    private GoogleUserRepo googleUserRepo;
    @Autowired
    private UserVerificationRepo userVerificationRepo;


    @BeforeAll
    void init() {
        postRepository.deleteAll();
        posts = new ArrayList<>();
        addUser1();
        addUser2();
        addGoogleUser();
    }

    @AfterEach
    void cleanUpEach() {
        reportedPostRepository.deleteAll();
    }

    @AfterAll
    void cleanUpAll() {
        postRepository.deleteAll();
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        googleUserRepo.deleteAll();
        userRepo.deleteAll();
    }

    void addUser1() {
        when(securityUtils.getCurrentUserName()).thenReturn("mocked User1");
        UserDTO userDTO1 = new UserDTO("greyEmail1@gmail.com", "mocked User1", "mock Pass 1");
        signup.signup(userDTO1);
        user1 = "mocked User1";
        createPostsForUser1();
    }

    private void createPostsForUser1() {
        for (int i = 0; i < 5; i++)
            posts.add(postRepository.save(Post.builder()
                    .postText("Some bad text" + i)
                    .user(userRepo.findByUsername(user1))
                    .postFeelings(Set.of(LOVE, HAPPY))
                    .build()));
    }

    void addUser2() {
        when(securityUtils.getCurrentUserName()).thenReturn("mocked User2");
        UserDTO userDTO2 = new UserDTO("greyEmail2@gmail.com", "mocked User2", "mock Pass 2");
        signup.signup(userDTO2);
        user2 = "mocked User2";
        createPostsForUser2();
    }

    private void createPostsForUser2() {
        for (int i = 0; i < 3; i++)
            posts.add(postRepository.save(Post.builder()
                    .postText("Some bad text" + i)
                    .user(userRepo.findByUsername(user2))
                    .postFeelings(Set.of(FEAR, ANGER))
                    .build()));
        for (int i = 0; i < 3; i++)
            posts.add(postRepository.save(Post.builder()
                    .postText("Some bad text other than the first bad text" + i)
                    .user(userRepo.findByUsername(user2))
                    .postFeelings(Set.of(ANXIOUS, SAD))
                    .build()));
    }

    void addGoogleUser() {
        when(securityUtils.getCurrentUserName()).thenReturn("mockGmail");
        UserDTO userG = new UserDTO("mockGmail@gmail.com", "mockGmail", "mockPas2");
        userG.externalID = "mockedGoogleID";
        userService.saveGoogleUser(userG);
        createPostsForGUser();
    }

    private void createPostsForGUser() {
        for (int i = 0; i < 3; i++)
            posts.add(postRepository.save(Post.builder()
                    .postText("Some bad text from google user" + i)
                    .user(userRepo.findByUsername("mockGmail"))
                    .postFeelings(Set.of(FEAR, ANGER))
                    .build()));
        for (int i = 0; i < 3; i++)
            posts.add(postRepository.save(Post.builder()
                    .postText("Some bad text from google user other than the first bad text" + i)
                    .user(userRepo.findByUsername("mockGmail"))
                    .postFeelings(Set.of(ANXIOUS, SAD))
                    .build()));
    }

    @Test
    void reportExistingPostUser1_shouldBeValid() {
        User user = userRepo.findByUsername("mocked User1");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        // loop over posts and report each one
        for (Post post : posts) {
            postService.report(post.getId());
            assertThat(reportedPostRepository.existsById(new ReportedPostId(post, user))).isTrue();
        }
    }

    @Test
    void reportExistingPostUser2_shouldBeValid() {
        User user = userRepo.findByUsername("mocked User2");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        // loop over posts and report each one
        for (Post post : posts) {
            postService.report(post.getId());
            assertThat(reportedPostRepository.existsById(new ReportedPostId(post, user))).isTrue();
        }
    }

    @Test
    void reportExistingPostUserG_shouldBeValid() {
        User user = userRepo.findByUsername("mockGmail");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        // loop over posts and report each one
        for (Post post : posts) {
            postService.report(post.getId());
            assertThat(reportedPostRepository.existsById(new ReportedPostId(post, user))).isTrue();
        }
    }

    @Test
    void duplicateReportExistingPostBasicUser_shouldThrowException() {
        User user = userRepo.findByUsername("mocked User1");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        // loop over posts and report each one
        for (Post post : posts) {
            postService.report(post.getId());
            Assertions.assertThrows(UserReportedPostBeforeException.class,
                    () -> postService.report(post.getId()));
        }
    }

    @Test
    void duplicateReportExistingPostGoogleUser_shouldThrowException() {
        User user = userRepo.findByUsername("mockGmail");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        // loop over posts and report each one
        for (Post post : posts) {
            postService.report(post.getId());
            Assertions.assertThrows(UserReportedPostBeforeException.class,
                    () -> postService.report(post.getId()));
        }
    }

    @Test
    void reportNotExistingPost_shouldThrowException() {
        UUID randomUUID = UUID.randomUUID();
        while (postRepository.existsById(randomUUID.toString()))
            randomUUID = UUID.randomUUID();
        UUID finalRandomUUID = randomUUID;

        User user = userRepo.findByUsername("mocked User1");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> postService.report(finalRandomUUID.toString()));
        assertEquals("Post not found", exception.getMessage());
    }

    @Test
    void getReportedPostsSmokeTest() {

        User user = userRepo.findByUsername("mocked User1");
        when(securityUtils.getCurrentUser()).thenReturn(user);

        // loop over posts and report each one
        for (Post post : posts) {
            postService.report(post.getId());
        }

        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(0).pageSize(5).build();
        Page<PostDTO> p = postService.getReportedPosts(postFilterDTO);
        assertThat(p.getContent()).hasSize(5);
    }
}
