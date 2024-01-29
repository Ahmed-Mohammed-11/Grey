package com.software.grey.services;

import com.software.grey.exceptions.exceptions.DataNotFoundException;
import com.software.grey.exceptions.exceptions.PostNotFoundException;
import com.software.grey.exceptions.exceptions.UserNotAuthorizedException;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.software.grey.models.enums.Feeling.*;
import static com.software.grey.models.enums.Feeling.SAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public class PostServiceDeleteTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepo userRepo;

    @MockBean
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @BeforeAll
    void init() throws InterruptedException {
        prepareDataUser1();
        prepareDataUser2();
    }

    @AfterAll
    void del() {
        postRepository.deleteAll();
    }

    void prepareDataUser1() throws InterruptedException {
        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName1");
        UserDTO userDTO1 = new UserDTO("mockEmail1@gmail.com", "mockedUserName1", "mockPas1");
        userService.save(userDTO1);
        List<Set<Feeling>> feelings = List.of(Set.of(LOVE), Set.of(LOVE, HAPPY), Set.of(SAD), Set.of(LOVE, HAPPY, SAD));
        for (int i = 0; i < 5; i++) {
            postService.add(PostDTO.builder().postText(i + " user1").postFeelings(feelings.get(i % feelings.size())).build());
            Thread.sleep(30);
        }
    }

    void prepareDataUser2() throws InterruptedException {
        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName2");
        UserDTO userDTO2 = new UserDTO("mockEmail2@gmail.com", "mockedUserName2", "mockPas2");
        userService.save(userDTO2);
        List<Set<Feeling>> feelings = List.of(Set.of(LOVE), Set.of(SAD));
        for (int i = 0; i < 3; i++) {
            postService.add(PostDTO.builder().postText(i + " user2").postFeelings(feelings.get(i % feelings.size())).build());
            Thread.sleep(30);
        }
    }

    @Test
        //delete post correctly
    void deletePostCorrectly() {
        //prepare mock post
        PostDTO postDTO = PostDTO.builder()
                .postText("this is a mocked text")
                .postFeelings(Set.of(LOVE, HAPPY)).build();

        //prepare mock user and save it
        UserDTO userDTO = new UserDTO("test2@gmail.com", "testUser2", "mock Pass 111");
        userService.save(userDTO);

        when(securityUtils.getCurrentUserName()).thenReturn("testUser2");
        //save the post created by the user testUser
        String postId = postService.add(postDTO);
        assertThat(postId).isNotNull();

        //find the user
        User user = userService.findByUserName("testUser2");
        assertThat(user).isNotNull();

        //delete the post
        when(securityUtils.getCurrentUserId()).thenReturn(user.getId());
        postService.delete(postId.toString());
        assertThat(postRepository.existsById(postId)).isFalse();
    }

    @Test
        //delete post when the user is not the owner
    void deletePostWhenUserIsNotTheOwner() {
        //prepare mock post
        PostDTO postDTO = PostDTO.builder()
                .postText("this is a mocked text")
                .postFeelings(Set.of(LOVE, HAPPY)).build();

        //prepare mock user and save it
        UserDTO userDTO = new UserDTO("theowner@gmail.com", "theOwner", "mock Pass 111");
        userService.save(userDTO);

        when(securityUtils.getCurrentUserName()).thenReturn("theOwner");
        //save the post created by the user testUser
        String postId = postService.add(postDTO);
        assertThat(postId).isNotNull();


        UserDTO userDTO2 = new UserDTO("hecker@gmail.com", "heckerUser", "mock Pass 111");
        userService.save(userDTO2);
        User user = userService.findByUserName("heckerUser");
        assertThat(user).isNotNull();

        //find the user id
        when(securityUtils.getCurrentUserId()).thenReturn(user.getId());
        //assert throwing the exception
        UserNotAuthorizedException exception = assertThrows(UserNotAuthorizedException.class, () -> postService.delete(postId.toString()));
        assertThat(exception.getMessage()).isEqualTo("You are not authorized to delete this post");
    }

    @Test
        //delete non existing post
    void deleteNonExistingPost() {
        //prepare mock user and save it
        UserDTO userDTO = new UserDTO("test@gmail.com", "testUser", "mock Pass 111");
        userService.save(userDTO);

        when(securityUtils.getCurrentUserName()).thenReturn("testUser");
        //save the post created by the user testUser
        String postId = "00000000-0000-0000-0000-000000000000";
        assertThat(postRepository.existsById(postId)).isFalse();

        //find the user
        User user = userService.findByUserName("testUser");
        assertThat(user).isNotNull();

        //delete the post
        when(securityUtils.getCurrentUserId()).thenReturn(user.getId());
        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> postService.delete(postId.toString()));
        assertThat(exception.getMessage()).isEqualTo("Post not found");
    }


    @Test
    void deletePostInvalidId() {
        //prepare mock user and save it
        UserDTO userDTO = new UserDTO("test3@gmail.com", "testUser3", "mock Pass 111");
        userService.save(userDTO);

        when(securityUtils.getCurrentUserName()).thenReturn("testUser3");

        //find the user
        User user = userService.findByUserName("testUser3");
        assertThat(user).isNotNull();

        //delete the post
        when(securityUtils.getCurrentUserId()).thenReturn(user.getId());
        Exception exception = assertThrows(PostNotFoundException.class, () -> postService.delete("invalidUUID"));
        assertThat(exception.getMessage()).isEqualTo("Post not found");
    }

    @Test
    void deletePostWithNullId() {
        //prepare mock user and save it
        UserDTO userDTO = new UserDTO("test4@gmail.com", "testUser4", "mock Pass 111");
        userService.save(userDTO);

        when(securityUtils.getCurrentUserName()).thenReturn("testUser4");

        //find the user
        User user = userService.findByUserName("testUser4");
        assertThat(user).isNotNull();

        //delete the post
        when(securityUtils.getCurrentUserId()).thenReturn(user.getId());
        Exception exception = assertThrows(NullPointerException.class, () -> postService.delete(null));

    }
}
