package com.software.grey.ServiceTest;


import com.software.grey.SavedPostEnum;
import com.software.grey.TestDataUtil.ObjectsBuilder;
import com.software.grey.controllers.SignupController;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.SavedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.models.mappers.UserMapper;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.SavedPostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.services.SavedPostService;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TestSavedPostService {

    @MockBean
    private SecurityUtils securityUtils;
    private final SavedPostService savedPostService;
    private final ObjectsBuilder objectsBuilder;
    private final UserRepo userRepository;
    private final PostRepository postRepository;
    private final SignupController signupController;
    private final SavedPostRepository savedPostRepository;
    private final UserMapper userMapper;

    @Autowired
    TestSavedPostService (SavedPostService savedPostService, ObjectsBuilder objectsBuilder1,
                          UserRepo userRepository, PostRepository postRepository,
                          SavedPostRepository savedPostRepository, SignupController signupController,
                          UserMapper userMapper) {
        this.savedPostService = savedPostService;
        this.objectsBuilder = objectsBuilder1;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.savedPostRepository = savedPostRepository;
        this.signupController = signupController;
        this.userMapper = userMapper;
    }

    @Test
    void testSavePostWithValidData() {

        // create user that will save the post
        User a = objectsBuilder.createUserA();
        userRepository.save(a);

        // create user to be the post author
        User b = objectsBuilder.createUserB();
        userRepository.save(b);

        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);

        // create a post to be saved
        Post postB = objectsBuilder.createPostB(b);
        postRepository.save(postB);

        // save the post and assert that it saved successfully
        SavedPostEnum savedPostEnum = savedPostService.toggleSavedPost(postB.getId());
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

        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);

        // create posts and save them
        Post postA = objectsBuilder.createPostA(a);
        Post postB = objectsBuilder.createPostB(b);
        postRepository.save(postA);
        postRepository.save(postB);

        // save post
        SavedPostEnum savedPostEnum = savedPostService.toggleSavedPost(postB.getId());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.SAVED);

        // assert that the post is saved
        Iterable<SavedPost> savedPost = savedPostRepository.findAll();
        assertThat(savedPost).hasSize(1);

        // un-save the post
        savedPostEnum = savedPostService.toggleSavedPost(postB.getId());
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

        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);

        // create the post but do not save it, so it will be not-found
        Post post = objectsBuilder.createPostA(b);

        SavedPostEnum savedPostEnum = savedPostService.toggleSavedPost(post.getId());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.NOT_FOUND);
    }

    @Test
    void testSavedPostServiceWithInvalidData() {
        User b = objectsBuilder.createUserB();
        Post post = objectsBuilder.createPostA(b);

        SavedPostEnum savedPostEnum = savedPostService.toggleSavedPost(post.getId());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.NOT_FOUND);
    }
}
