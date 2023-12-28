package com.software.grey.controllers;

import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.ReportedPost;
import com.software.grey.models.entities.ReportedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.*;
import com.software.grey.utils.EndPoints;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Set;

import static com.software.grey.models.enums.Feeling.LOVE;
import static com.software.grey.models.enums.Feeling.SAD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportedPostActionsControllerTest {

    @Autowired
    private SignupController signupController;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReportedPostRepository reportedPostRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserVerificationRepo userVerificationRepo;
    @Autowired
    private BasicUserRepo basicUserRepo;
    @Autowired
    private MockMvc mockMvc;

    private User reporter;
    private ArrayList<Post> posts;

    @BeforeAll
    void init() {
        UserDTO userA = new UserDTO("mockusera@gmail.com", "mockusera", "Write unethical post");
        UserDTO userB = new UserDTO("mockuserb@gmail.com", "mockuserb", "report unethical post");
        signupController.signup(userA);
        signupController.signup(userB);

        posts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            posts.add(postRepository.save(Post.builder()
                    .postText("unethical post")
                    .user(userRepo.findByUsername("mockusera"))
                    .postFeelings(Set.of(LOVE, SAD))
                    .build()));
        }

        reporter = userRepo.findByUsername("mockuserb");

        for (int i = 0; i < 7; i++) {
            reportedPostRepository.save(ReportedPost.builder()
                    .post(posts.get(i))
                    .reporter(reporter)
                    .build());
        }
    }

    @AfterAll
    void cleanup () {
        reportedPostRepository.deleteAll();
        postRepository.deleteAll();
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    @WithMockUser(username = "greymod", authorities = "MODERATOR")
    void moderatorDeletePost () throws Exception {

        int postIdx = 0;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(EndPoints.POST + EndPoints.REPORT_POST + "/" + posts.get(postIdx).getId())
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().string("Post was deleted successfully!")
        );

        assertThat(reportedPostRepository.existsById(new ReportedPostId(posts.get(postIdx), reporter))).isFalse();
        assertThat(postRepository.existsById(posts.get(postIdx).getId())).isFalse();
    }

    @Test
    @WithMockUser(username = "greymod", authorities = "ADMIN")
    void adminDeletePost () throws Exception {

        int postIdx = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(EndPoints.POST + EndPoints.REPORT_POST + "/" + posts.get(postIdx).getId())
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().string("Post was deleted successfully!")
        );

        assertThat(reportedPostRepository.existsById(new ReportedPostId(posts.get(postIdx), reporter))).isFalse();
        assertThat(postRepository.existsById(posts.get(postIdx).getId())).isFalse();
    }

    @Test
    @WithMockUser(username = "greymod", roles = "USER")
    void userDeletePost () throws Exception {

        int postIdx = 2;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(EndPoints.POST + EndPoints.REPORT_POST + "/" + posts.get(postIdx).getId())
        ).andExpect(
                status().isForbidden()
        );

        assertThat(reportedPostRepository.existsById(new ReportedPostId(posts.get(postIdx), reporter))).isTrue();
        assertThat(postRepository.existsById(posts.get(postIdx).getId())).isTrue();
    }

    @Test
    @WithMockUser(username = "greymod", authorities = "ADMIN")
    void adminRemovePost () throws Exception {

        int postIdx = 3;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(EndPoints.POST + EndPoints.REMOVE_REPORTED_POST + "/" + posts.get(postIdx).getId())
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().string("Post is safe!")
        );

        assertThat(reportedPostRepository.existsById(new ReportedPostId(posts.get(postIdx), reporter))).isFalse();
        assertThat(postRepository.existsById(posts.get(postIdx).getId())).isTrue();
    }

    @Test
    @WithMockUser(username = "greymod", authorities = "MODERATOR")
    void moderatorRemovePost () throws Exception {

        int postIdx = 4;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(EndPoints.POST + EndPoints.REMOVE_REPORTED_POST + "/" + posts.get(postIdx).getId())
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().string("Post is safe!")
        );

        assertThat(reportedPostRepository.existsById(new ReportedPostId(posts.get(postIdx), reporter))).isFalse();
        assertThat(postRepository.existsById(posts.get(postIdx).getId())).isTrue();
    }

    @Test
    @WithMockUser(username = "greymod", roles = "USER")
    void userRemovePost () throws Exception {

        int postIdx = 5;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(EndPoints.POST + EndPoints.REMOVE_REPORTED_POST + "/" + posts.get(postIdx).getId())
        ).andExpect(
                status().isForbidden()
        );

        assertThat(reportedPostRepository.existsById(new ReportedPostId(posts.get(postIdx), reporter))).isTrue();
        assertThat(postRepository.existsById(posts.get(postIdx).getId())).isTrue();
    }

    @Test
    @WithMockUser(username = "greymod", authorities = "MODERATOR")
    void moderatorDeletePostWithInvalidId () throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(EndPoints.POST + EndPoints.REPORT_POST + "/invalidId")
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string("Post already deleted")
        );
    }

    @Test
    @WithMockUser(username = "greymod", authorities = "MODERATOR")
    void moderatorDeleteUnreportedPost () throws Exception {

        int postIdx = 8;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(EndPoints.POST + EndPoints.REMOVE_REPORTED_POST + "/" + posts.get(postIdx).getId())
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string("Post already deleted")
        );

        assertThat(reportedPostRepository.existsById(new ReportedPostId(posts.get(postIdx), reporter))).isFalse();
        assertThat(postRepository.existsById(posts.get(postIdx).getId())).isTrue();
    }
}
