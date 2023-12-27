package com.software.grey.services;

import com.software.grey.controllers.SignupController;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.ReportedPost;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.*;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Set;

import static com.software.grey.models.enums.Feeling.LOVE;
import static com.software.grey.models.enums.Feeling.SAD;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportedPostActionsTest {
    @MockBean
    private SecurityUtils securityUtils;
    @Autowired
    private SignupController signupController;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private ReportedPostRepository reportedPostRepository;
    @Autowired
    private UserVerificationRepo userVerificationRepo;
    @Autowired
    private BasicUserRepo basicUserRepo;
    @Autowired
    private UserRepo userRepo;
    private final ArrayList<Post> posts = new ArrayList<>();

    @BeforeAll
    void init() {
        UserDTO userA = new UserDTO("mockusera@gmail.com", "mockusera", "Write unethical post");
        UserDTO userB = new UserDTO("mockuserb@gmail.com", "mockuserb", "report unethical post");
        signupController.signup(userA);
        signupController.signup(userB);

        for (int i = 0; i < 10; i++) {
            posts.add(postRepository.save(Post.builder()
                    .postText("unethical post")
                    .user(userRepo.findByUsername("mockusera"))
                    .postFeelings(Set.of(LOVE, SAD))
                    .build()));
        }

        User userb = userRepo.findByUsername("mockuserb");

        for (Post post : posts) {
            reportedPostRepository.save(ReportedPost.builder().post(post).reporter(userb).build());
        }
    }

    @AfterAll
    void cleanUp() {
        reportedPostRepository.deleteAll();
        postRepository.deleteAll();
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void shouldReturnAllReportedPosts() {
        assertEquals(10, reportedPostRepository.findAll().size());
    }

    @Test
    void DeleteReportedPost() {
        Post post = posts.get(0);
        postService.deleteReportedPost(post.getId());
        assertEquals(9, reportedPostRepository.findAll().size());
    }

}
