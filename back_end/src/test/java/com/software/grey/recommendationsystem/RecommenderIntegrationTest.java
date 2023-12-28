package com.software.grey.recommendationsystem;

import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecommenderIntegrationTest {

    @Autowired
    private Recommender recommender;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepo userRepo;

    @MockBean
    private SecurityUtils securityUtils;

    private User user1;
    private User user2;
    private User user3;

    @BeforeAll
    void setup() {
        // Create a test user
        user1 = User.builder().email("RecommenderIntegration@example.com").username("RecommenderIntegration").build();
        user2 = User.builder().email("RecommenderIntegration2@example.com").username("RecommenderIntegration2").build();
        user3 = User.builder().email("RecommenderIntegration3@example.com").username("RecommenderIntegration3").build();
        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        for (int i = 0; i < 30; i++) {
            Set<Feeling> set = new TreeSet<>();
            set.add(Feeling.HAPPY);
            Post post = Post.builder()
                    .postText("Osama is happy awy awy awy")
                    .user(user2)
                    .postFeelings(set)
                    .postTime(Timestamp.from(Instant.now()))
                    .build();
            postRepository.save(post);
        }
        for (int i = 0; i < 30; i++) {
            Set<Feeling> set = new TreeSet<>();
            set.add(Feeling.ANXIOUS);
            Post post = Post.builder()
                    .postText("Anxious")
                    .user(user2)
                    .postFeelings(set)
                    .postTime(Timestamp.from(Instant.now()))
                    .build();
            postRepository.save(post);
        }
        for (int i = 0; i < 30; i++) {
            Set<Feeling> set = new TreeSet<>();
            set.add(Feeling.SAD);
            Post post = Post.builder()
                    .postText("Sad")
                    .user(user2)
                    .postFeelings(set)
                    .postTime(Timestamp.from(Instant.now()))
                    .build();
            postRepository.save(post);
        }
    }

    @AfterAll
    void cleanup() {
        // Cleanup: Delete test data
        postRepository.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void testRecommendIntegration() {
        // Assuming user1 is sad
        Set<Feeling> sadFeelingSet = new TreeSet<>();
        sadFeelingSet.add(Feeling.SAD);
        Post sadPost = Post.builder().postText("Feeling sad").user(user1).postFeelings(sadFeelingSet).build();
        postRepository.save(sadPost);

        // Mock the current user for the securityUtils
        when(securityUtils.getCurrentUser()).thenReturn(user1);

        PostFilterDTO postFilterDTO = new PostFilterDTO();
        postFilterDTO.setPageNumber(0);
        postFilterDTO.setPageSize(10);

        // Act
        List<Post> recommendedPosts = recommender.recommend(postFilterDTO);

        // Assert
        assertEquals(10, recommendedPosts.size());
    }

    @Test
    void testRecommendIntegrationWhenNoPosts() {
        // Assuming user1 has no posts
        when(securityUtils.getCurrentUser()).thenReturn(user3);

        PostFilterDTO postFilterDTO = new PostFilterDTO();
        postFilterDTO.setPageNumber(0);
        postFilterDTO.setPageSize(10);

        // Act
        List<Post> recommendedPosts = recommender.recommend(postFilterDTO);

        // Assert
        assertTrue(recommendedPosts.isEmpty());
    }

    @Test
    void testRecommendIntegrationWithPaging() {
        // Assuming user2 has posts with different feelings
        when(securityUtils.getCurrentUser()).thenReturn(user1);

        PostFilterDTO postFilterDTO = new PostFilterDTO();
        postFilterDTO.setPageNumber(1); // Second page
        postFilterDTO.setPageSize(10);

        Set<Feeling> set = new TreeSet<>();
        set.add(Feeling.SAD);
        Post postTemp = Post.builder()
                .postText("Sad")
                .user(user1)
                .postFeelings(set)
                .postTime(Timestamp.from(Instant.now()))
                .build();
        postRepository.save(postTemp);


        // Act
        List<Post> recommendedPosts = recommender.recommend(postFilterDTO);

        // Assert
        assertTrue(recommendedPosts.size() > 0);
        // Ensure that posts from the second page are included
        assertTrue(recommendedPosts.stream().anyMatch(post -> post.getPostFeelings().contains(Feeling.ANXIOUS)));
    }

}
