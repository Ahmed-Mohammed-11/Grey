package com.software.grey.repositories;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void del() {
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

    @Test
    void collaborativeNoPosts() {
        // Ensure that a user with no 'LOVE' posts returns an empty list
        PageRequest pageable = PageRequest.of(0, 20);
        List<Post> result = postRepository.findByCollaborativeFiltering("INSPIRATION", user1.getId(), pageable);
        assertTrue(result.isEmpty());
    }

    @Test
    void collaborativeMultipleUsers() {
        // Save a 'LOVE' post for user1
        Set<Feeling> feelings1 = new TreeSet<>();
        feelings1.add(Feeling.LOVE);
        Post post1 = Post.builder().postText("LOVE").user(user1).postFeelings(feelings1).build();
        postRepository.save(post1);

        // Save a 'LOVE' post for user2
        Set<Feeling> feelings2 = new TreeSet<>();
        feelings2.add(Feeling.LOVE);
        Post post2 = Post.builder().postText("LOVE").user(user2).postFeelings(feelings2).build();
        postRepository.save(post2);

        // Save a 'HAPPY' post for user1
        Set<Feeling> feelings3 = new TreeSet<>();
        feelings3.add(Feeling.HAPPY);
        Post post3 = Post.builder().postText("HAPPY").user(user1).postFeelings(feelings3).build();
        postRepository.save(post3);

        // Save a 'SAD' post for user2
        Set<Feeling> feelings4 = new TreeSet<>();
        feelings4.add(Feeling.SAD);
        Post post4 = Post.builder().postText("SAD").user(user1).postFeelings(feelings4).build();
        postRepository.save(post4);

        PageRequest pageable = PageRequest.of(0, 20);
        // Ensure that collaborative filtering works for multiple users
        List<Post> result = postRepository.findByCollaborativeFiltering("LOVE", user2.getId(), pageable);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(post -> post.getPostFeelings().contains(Feeling.HAPPY)));
        assertTrue(result.stream().anyMatch(post -> post.getPostFeelings().contains(Feeling.SAD)));
    }

    @Test
    void collaborativePagination() {
        // Save multiple 'LOVE' posts
        for (int i = 0; i < 4; i++) {
            Set<Feeling> feelings2 = new TreeSet<>();
            feelings2.add(Feeling.SAD);
            Post post2 = Post.builder().postText("SAD" + i).user(user2).postFeelings(feelings2).build();
            postRepository.save(post2);
        }
        Set<Feeling> feelings = new TreeSet<>();
        feelings.add(Feeling.LOVE);
        Post post = Post.builder().postText("LOVE").user(user2).postFeelings(feelings).build();
        postRepository.save(post);
        postRepository.save(Post.builder().postText("LOVE").user(user1).postFeelings(Collections.singleton(Feeling.LOVE)).build());


        PageRequest pageable = PageRequest.of(0, 2);
        // Ensure that pagination is working correctly
        List<Post> result = postRepository.findByCollaborativeFiltering("LOVE", user1.getId(), pageable);
        assertEquals(2, result.size());
    }

    @Test
    void collaborativeNoMatchingFeeling() {
        // Save a 'LOVE' post for user1
        Set<Feeling> feelings1 = new TreeSet<>();
        feelings1.add(Feeling.LOVE);
        Post post1 = Post.builder().postText("LOVE").user(user1).postFeelings(feelings1).build();
        postRepository.save(post1);

        // Save 10 'SAD' posts for user1
        for (int i = 0; i < 10; i++) {
            Set<Feeling> feelings = new TreeSet<>();
            feelings.add(Feeling.SAD);
            Post post = Post.builder().postText("SAD" + i).user(user1).postFeelings(feelings).build();
            postRepository.save(post);
        }

        PageRequest pageable = PageRequest.of(0, 20);
        // Ensure that no 'SAD' posts are returned when searching for 'HAPPY'
        List<Post> result = postRepository.findByCollaborativeFiltering("HAPPY", user1.getId(), pageable);
        assertTrue(result.isEmpty());
    }

    @Test
    void collaborativeMixedFeelings() {
        // Save a post with 'LOVE' and 'SAD' feelings for user1
        Set<Feeling> feelings1 = new TreeSet<>();
        feelings1.add(Feeling.LOVE);
        feelings1.add(Feeling.SAD);
        Post post1 = Post.builder()
                .postText("Mixed Feelings")
                .user(user1)
                .postFeelings(feelings1)
                .postTime(Timestamp.from(Instant.now()))
                .build();
        postRepository.save(post1);

        // Save a 'LOVE' post for user2
        Set<Feeling> feelings2 = new TreeSet<>();
        feelings2.add(Feeling.LOVE);
        Post post2 = Post.builder()
                .postText("LOVE")
                .user(user2)
                .postFeelings(feelings2)
                .postTime(Timestamp.from(Instant.now()))
                .build();
        postRepository.save(post2);

        PageRequest pageable = PageRequest.of(0, 20);
        List<Post> result = postRepository.findByCollaborativeFiltering("LOVE", user2.getId(), pageable);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getPostFeelings().size());
    }


}