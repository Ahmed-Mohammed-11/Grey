package com.software.grey.ServiceTest;


import com.software.grey.SavedPostEnum;
import com.software.grey.TestDataUtil.ObjectsBuilder;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.SavedPostRepository;
import com.software.grey.repositories.UserRepository;
import com.software.grey.services.SavedPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TestSavedPostService {
    private final SavedPostService savedPostService;
    private final ObjectsBuilder objectsBuilder;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final SavedPostRepository savedPostRepository;

    @Autowired
    TestSavedPostService (SavedPostService savedPostService, ObjectsBuilder objectsBuilder1,
                          UserRepository userRepository, PostRepository postRepository,
                          SavedPostRepository savedPostRepository) {
        this.savedPostService = savedPostService;
        this.objectsBuilder = objectsBuilder1;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.savedPostRepository = savedPostRepository;
    }

    @Test
    void testSavePostWithValidData() {
        // create users and save them
        User a = objectsBuilder.createUserA();
        User b = objectsBuilder.createUserB();
        userRepository.save(a);
        userRepository.save(b);

        // create posts and save them
        Post postA = objectsBuilder.createPostA(a);
        Post postB = objectsBuilder.createPostB(b);
        postRepository.save(postA);
        postRepository.save(postB);

        SavedPostEnum savedPostEnum = savedPostService.saveUnsavePost(postB.getId());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.SAVED);

        // get all saved posts
        Iterable<SavedPost> savedPost = savedPostRepository.findAll();
        assertThat(savedPost).hasSize(1);

        // check the retrieved data
        SavedPost s = savedPost.iterator().next();

        // assert post data
        Post retrievedPost = s.getPost();
        assertThat(retrievedPost.getId()).isEqualTo(postB.getId());
        assertThat(retrievedPost.getUser().getUsername()).isEqualTo(b.getUsername());
        assertThat(retrievedPost.getText()).isEqualTo(postB.getText());

        // assert user data
        User retrievedUser = s.getUser();
        assertThat(retrievedUser.getId()).isEqualTo(a.getId());
        assertThat(retrievedUser.getEmail()).isEqualTo(a.getEmail());
        assertThat(retrievedUser.getUsername()).isEqualTo(a.getUsername());
    }

    @Test
    void testUnSavePostWithValidData() {
        // create users and save them
        User a = objectsBuilder.createUserA();
        User b = objectsBuilder.createUserB();
        userRepository.save(a);
        userRepository.save(b);

        // create posts and save them
        Post postA = objectsBuilder.createPostA(a);
        Post postB = objectsBuilder.createPostB(b);
        postRepository.save(postA);
        postRepository.save(postB);

        // save post
        SavedPostEnum savedPostEnum = savedPostService.saveUnsavePost(postB.getId());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.SAVED);

        // assert that the post is saved
        Iterable<SavedPost> savedPost = savedPostRepository.findAll();
        assertThat(savedPost).hasSize(1);

        // un-save the post
        savedPostEnum = savedPostService.saveUnsavePost(postB.getId());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.REMOVED);

        savedPost = savedPostRepository.findAll();
        assertThat(savedPost).isEmpty();

        // assert that user a and postB are not removed
        Optional<User> user = userRepository.findById(a.getId());
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(a.getUsername());

        Optional<Post> post = postRepository.findById(postB.getId());
        assertThat(post).isPresent();
        assertThat(post.get().getText()).isEqualTo(postB.getText());
    }

    @Test
    void testSavedPostServiceWithInvalidPost() {
        User a = objectsBuilder.createUserA();
        userRepository.save(a);
        User b = objectsBuilder.createUserB();
        userRepository.save(b);

        Post post = objectsBuilder.createPostA(b);

        SavedPostEnum savedPostEnum = savedPostService.saveUnsavePost(post.getId());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.NOT_FOUND);
    }

    @Test
    void testSavedPostServiceWithInvalidData() {
        User b = objectsBuilder.createUserB();

        Post post = objectsBuilder.createPostA(b);

        SavedPostEnum savedPostEnum = savedPostService.saveUnsavePost(post.getId());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.NOT_FOUND);
    }
}
