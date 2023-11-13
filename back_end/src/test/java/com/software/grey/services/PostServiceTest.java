package com.software.grey.services;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.mappers.PostMapper;
import com.software.grey.repositories.PostRepository;
import com.software.grey.services.implementations.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;


    @Test
    void addPostCorrectly() throws Exception {
        when(postRepository.save(any(Post.class))).thenReturn(new Post());
        when(postMapper.toPost(any(PostDTO.class))).thenReturn(new Post());
        postService.add(new PostDTO());
        verify(postMapper,times(1)).toPost(any(PostDTO.class));
        verify(postRepository,times(1)).save(any(Post.class));
    }

}