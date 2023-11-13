package com.software.grey.controllers;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.EndPoints;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void addPostCorrectly() throws Exception {
         when(postService.add(any(PostDTO.class))).thenReturn(null);
         mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content("""
                                 {"postText":"this is the mock post text"}"""))
                 .andExpect(status().isCreated());
         verify(postService,times(1)).add(any(PostDTO.class));
    }

    @Test
     void addPostWithTextExceedMaxLength() throws Exception {
         PostDTO postDTO = PostDTO.builder().postText(StringUtils.repeat("*", 5001)).build();

         mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(String.valueOf(postDTO)))
                 .andExpect(status().isBadRequest());
    }

    @Test
    void addPostWithEmptyText() throws Exception {
        PostDTO postDTO = PostDTO.builder().postText("").build();

        mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(postDTO)))
                .andExpect(status().isBadRequest());
    }
}