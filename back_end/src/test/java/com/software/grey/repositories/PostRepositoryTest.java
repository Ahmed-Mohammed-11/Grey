package com.software.grey.repositories;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepo userRepo;
    private User user1;
    private User user2;

    @BeforeAll
    void init() {
        user1 = User.builder().email("postRepo@example.com").username("postRepo").build();
        user2 = User.builder().email("postRepo2@example.com").username("postRepo2").build();
        userRepo.save(user1);
        userRepo.save(user2);
    }

    @AfterEach
    void del(){
        postRepository.deleteAll();
    }

    @Test
    void findCountOf1FeelingsByUser() {
        Set<Feeling> set = new TreeSet<>();
        set.add(Feeling.FEAR);
        Post post = Post.builder().postText("Ta3ban").user(user1).postFeelings(set).build();
        postRepository.save(post);

        List<FeelingCountProjection> s = postRepository.findCountOfFeelingsByUser(user1.getId());

        assertEquals(Feeling.FEAR, s.get(0).getFeeling());
        assertEquals(1, s.get(0).getFeelingCount());
    }

    @Test
    void findCountOfFeelingsByUser_ThreePosts_TwoLoveOneAnxious() {
        // Add 2 posts for user 1 with LOVE and 1 post for user2 with 1 ANXIOUS
        Set<Feeling> loveFeelings = new TreeSet<>();
        loveFeelings.add(Feeling.LOVE);
        Set<Feeling> anxiousFeelings = new TreeSet<>();
        anxiousFeelings.add(Feeling.ANXIOUS);

        Post post1 = Post.builder().postText("Love post 1").user(user1).postFeelings(loveFeelings).build();
        Post post2 = Post.builder().postText("Love post 2").user(user1).postFeelings(loveFeelings).build();
        Post post3 = Post.builder().postText("Anxious post").user(user2).postFeelings(anxiousFeelings).build();

        postRepository.saveAll(List.of(post1, post2, post3));
        List<FeelingCountProjection> result = postRepository.findCountOfFeelingsByUser(user1.getId());

        // Assert that count of LOVE is 2 and count of feelings is 1
        assertEquals(1, result.size());
        assertEquals(Feeling.LOVE, result.get(0).getFeeling());
        assertEquals(2, result.get(0).getFeelingCount());
    }

    @Test
    void findCountOfFeelingsByUser_NoPosts() {
        // Test for a user that does not have posts
        List<FeelingCountProjection> result = postRepository.findCountOfFeelingsByUser(user1.getId());

        // Assert that the result is an empty list
        assertTrue(result.isEmpty());
    }

    @Test
    void whenMoreThanLimitPosts_ShouldGetRecent50() {
        // Add more than 50 posts for user 1
        Set<Feeling> feelings = new TreeSet<>();
        feelings.add(Feeling.LOVE);

        for (int i = 1; i <= 60; i++) {
            Post post = Post.builder().postText("Post " + i).user(user1).postFeelings(feelings).build();
            postRepository.save(post);
        }

        // Get the recent posts for user 1 and assert that only the latest 50 are returned
        List<FeelingCountProjection> result = postRepository.findCountOfFeelingsByUser(user1.getId());

        assertEquals(1, result.size());
        assertEquals(Feeling.LOVE, result.get(0).getFeeling());
        assertEquals(5, result.get(0).getFeelingCount());
    }
}