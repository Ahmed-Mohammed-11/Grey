package com.software.grey.controllers;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.EndPoints;
import com.software.grey.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Set;
import java.util.stream.Stream;

import static com.software.grey.models.enums.Feeling.HAPPY;
import static com.software.grey.models.enums.Feeling.LOVE;
import static com.software.grey.utils.JsonUtil.asJsonString;
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

    @MockBean
    private SecurityUtils securityUtils;

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

    @ParameterizedTest
    @MethodSource("paginationOfDiaryPostsFailParameters")
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void getDiaryOfUserWithFailPagination(Integer pageNumber, Integer pageSize, ResultMatcher status) throws Exception {
        PostFilterDTO postFilterDTO = PostFilterDTO.builder().pageNumber(pageNumber).pageSize(pageSize).build();
        mockMvc.perform(post(EndPoints.POST + EndPoints.GET_DIARY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postFilterDTO)))
                .andExpect(status);
        verify(postService, never()).getAll(any(PostFilterDTO.class));
    }

    static Stream<Arguments> paginationOfDiaryPostsFailParameters() {
        return Stream.of(
                Arguments.of(0, 0, status().isBadRequest()),// page size must be more than 0
                Arguments.of(-1, 1, status().isBadRequest()), // page number must be at least 0
                Arguments.of(null, 4, status().isBadRequest()),// page number must not be null
                Arguments.of(4, null, status().isBadRequest()),// page size must not be null
                Arguments.of(null, null, status().isBadRequest())// page number and size must not be null
        );
    }

    @ParameterizedTest
    @MethodSource("paginationOfDiaryPostsAcceptParameters")
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void getDiaryOfUserWithAcceptPagination(Integer pageNumber, Integer pageSize, ResultMatcher status) throws Exception {
        PostFilterDTO postFilterDTO = PostFilterDTO.builder().pageNumber(pageNumber).pageSize(pageSize).build();
        mockMvc.perform(post(EndPoints.POST + EndPoints.GET_DIARY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postFilterDTO)))
                .andExpect(status);
        verify(postService, times(1)).getAll(any(PostFilterDTO.class));
    }

    static Stream<Arguments> paginationOfDiaryPostsAcceptParameters() {
        return Stream.of(
                Arguments.of(0, 1, status().isOk()),
                Arguments.of(10, 10, status().isOk())
        );
    }
}