package com.software.grey.services;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.repositories.PostRepository;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;
import java.util.UUID;

import static com.software.grey.models.enums.Feeling.HAPPY;
import static com.software.grey.models.enums.Feeling.LOVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @MockBean
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;


    @Test
    void addPostCorrectly() throws Exception {

        PostDTO postDTO = PostDTO.builder()
                .postText("this is a mocked text")
                .postFeelings(Set.of(LOVE, HAPPY)).build();

        UserDTO userDTO = new UserDTO("mockEmail1@gmail.com", "mockedUserName1","mockPas1");

        userService.save(userDTO);

        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName");

        UUID postId = postService.add(postDTO);

        assertThat(postId).isNotNull();
        assertThat(postRepository.existsById(postId)).isTrue();
        Post retrievedPost = postService.findPostById(postId);
        assertThat(retrievedPost.getPostFeelings()).hasSize(2).contains(LOVE, HAPPY);
    }
}
