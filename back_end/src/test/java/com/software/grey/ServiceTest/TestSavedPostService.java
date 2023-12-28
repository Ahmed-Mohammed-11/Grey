package com.software.grey.ServiceTest;

import com.software.grey.TestDataUtil.ObjectsBuilder;
import com.software.grey.controllers.SignupController;
import com.software.grey.exceptions.exceptions.UserIsAuthorException;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.*;
import com.software.grey.services.SavedPostService;
import com.software.grey.services.implementations.PostServiceImpl;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestSavedPostService {

    @MockBean
    private SecurityUtils securityUtils;
    private final SavedPostService savedPostService;
    private final ObjectsBuilder objectsBuilder;
    private final UserRepo userRepository;
    private final PostRepository postRepository;
    private final SignupController signupController;
    private final SavedPostRepository savedPostRepository;
    private final UserVerificationRepo userVerificationRepo;
    private final BasicUserRepo basicUserRepo;

    @Autowired
    TestSavedPostService(SavedPostService savedPostService, ObjectsBuilder objectsBuilder1,
                         UserRepo userRepository, PostRepository postRepository,
                         SavedPostRepository savedPostRepository, SignupController signupController,
                         UserVerificationRepo userVerificationRepo, BasicUserRepo basicUserRepo, PostServiceImpl postService) {
        this.savedPostService = savedPostService;
        this.objectsBuilder = objectsBuilder1;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.savedPostRepository = savedPostRepository;
        this.signupController = signupController;
        this.userVerificationRepo = userVerificationRepo;
        this.basicUserRepo = basicUserRepo;
    }

    @BeforeAll
    void init() throws InterruptedException {
        UserDTO myUser = new UserDTO("mockEmailSave@gmail.com", "testUserSave", "mock Password test");
        signupController.signup(myUser);


        UserDTO myUser2 = new UserDTO("b.mail@gmail.com", "userB", "Omar Tammam");
        signupController.signup(myUser2);

        User b = userRepository.findByUsername("userB");
        Post postB = objectsBuilder.createPostB(b);
        postRepository.save(postB);
    }

    @AfterAll
    void cleanUp() {
        postRepository.deleteAll();
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSavePostWithValidData() {
        User a = userRepository.findByUsername("testUserSave");
        User b = userRepository.findByUsername("userB");
        Post postB = postRepository.findByUser(b);
        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);
        // save the post and assert that it saved successfully
        String result = savedPostService.toggleSavedPost(postB.getId());
        assertThat(result).isEqualTo("Saved successfully");

        // get all saved posts
        Iterable<SavedPost> savedPost = savedPostRepository.findAll();
        assertThat(savedPost).hasSize(1);

        // check the retrieved data
        SavedPost s = savedPost.iterator().next();

        // assert post data
        Post retrievedPost = s.getPost();
        assertThat(retrievedPost.getId()).isEqualTo(postB.getId());
        assertThat(retrievedPost.getUser().getUsername()).isEqualTo(b.getUsername());
        assertThat(retrievedPost.getPostText()).isEqualTo(postB.getPostText());

        // assert user data
        User retrievedUser = s.getUser();
        assertThat(retrievedUser.getId()).isEqualTo(a.getId());
        assertThat(retrievedUser.getEmail()).isEqualTo(a.getEmail());
        assertThat(retrievedUser.getUsername()).isEqualTo(a.getUsername());
        savedPostRepository.deleteAll();
    }

    @Test
    void testUnSavePostWithValidData() {
        User a = userRepository.findByUsername("testUserSave");
        User b = userRepository.findByUsername("userB");
        Post postB = postRepository.findByUser(b);
        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);
        // save the post and assert that it saved successfully
        String result = savedPostService.toggleSavedPost(postB.getId());
        assertThat(result).isEqualTo("Saved successfully");

        // assert that the post is saved
        Iterable<SavedPost> savedPost = savedPostRepository.findAll();
        assertThat(savedPost).hasSize(1);

        // un-save the post
        result = savedPostService.toggleSavedPost(postB.getId());
        assertThat(result).isEqualTo("Removed successfully");

        savedPost = savedPostRepository.findAll();
        assertThat(savedPost).isEmpty();

        // assert that user a and postB are not removed
        Optional<User> user = userRepository.findById(a.getId());
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(a.getUsername());

        Optional<Post> post = postRepository.findById(postB.getId());
        assertThat(post).isPresent();
        assertThat(post.get().getPostText()).isEqualTo(postB.getPostText());
        savedPostRepository.deleteAll();
    }


    @Test
    void testSaveMyPost() {
        User b = userRepository.findByUsername("userB");
        Post postB = postRepository.findByUser(b);
        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(b);

        UserIsAuthorException ex = assertThrows(UserIsAuthorException.class,
                () -> savedPostService.toggleSavedPost(postB.getId()));

        assertThat(ex.getMessage()).isEqualTo("You have written this post");
    }

    @Test
    void testSavedPostServiceWithInvalidPost() {
        // create user that will save the post
        User a = userRepository.findByUsername("testUser");

        // create user to be the post author
        User b = userRepository.findByUsername("userB");

        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);

        // create the post but do not save it, so it will be not-found
        Post post = postRepository.findByUser(b);
        String postId = post.getId();
        postRepository.delete(post);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> savedPostService.toggleSavedPost(postId));

        assertThat(ex.getMessage()).isEqualTo("Invalid arguments");
    }
}