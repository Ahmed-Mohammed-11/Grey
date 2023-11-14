package com.software.grey.controllers;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.enums.Feeling;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.EndPoints;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static com.software.grey.models.enums.Feeling.HAPPY;
import static com.software.grey.models.enums.Feeling.LOVE;
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
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void addPostCorrectly() throws Exception {
         when(postService.add(any(PostDTO.class))).thenReturn(null);
         mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content("""
                                 {
                                 "postText":"this is the mock post text",
                                 "postFeelings":["LOVE"]
                                 }"""))
                 .andExpect(status().isCreated());
         verify(postService,times(1)).add(any(PostDTO.class));
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
     void addPostWithTextExceedMaxLength() throws Exception {
         String txt = StringUtils.repeat("*", 5001);

         mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(String.format("""
                                 {
                                 "postText":%s,
                                 "postFeelings":["LOVE"]
                                 }""",txt)))
                 .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void addPostWithNullText() throws Exception {
        PostDTO postDTO = PostDTO.builder().postText("").build();

        mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                 "postFeelings":["LOVE"]
                                 }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void addPostWithEmptyText() throws Exception {
        PostDTO postDTO = PostDTO.builder().postText("").build();

        mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                 "postText":"",
                                 "postFeelings":["LOVE"]
                                 }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void addPostWithoutFeelings() throws Exception {
        PostDTO postDTO = PostDTO.builder().postText("post text").build();

        mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                 "postText":"this is the mock post text"
                                 }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void addPostWithEmptyFeelingsSet() throws Exception {
        PostDTO postDTO = PostDTO.builder().postText("post text").build();

        mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                 "postText":"this is the mock post text",
                                 "postFeelings":[]
                                 }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void addPostWithMoreThanOneFeeling() throws Exception {
        PostDTO postDTO = PostDTO.builder().postText("post text").postFeelings(Set.of(LOVE, HAPPY)).build();
        String out = postDTO.toString();
        mockMvc.perform(post(EndPoints.POST + EndPoints.ADD_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                 "postText":"this is the mock post text",
                                 "postFeelings":["LOVE","HAPPY"]
                                 }"""))
                .andExpect(status().isCreated());
        verify(postService,times(1)).add(any(PostDTO.class));
    }
}