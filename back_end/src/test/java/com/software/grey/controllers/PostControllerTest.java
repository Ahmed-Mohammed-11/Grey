package com.software.grey.controllers;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.enums.Feeling;
import com.software.grey.services.SavedPostService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.software.grey.models.enums.Feeling.*;
import static com.software.grey.utils.JsonUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private SavedPostService savedPostService;

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
    void getDiaryOfUserWithFailPagination(Integer pageNumber,
                                          Integer pageSize,
                                          Integer day,
                                          Integer month,
                                          Integer year) throws Exception {
        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(pageNumber).pageSize(pageSize).day(day).month(month).year(year).build();
        mockMvc.perform(post(EndPoints.POST + EndPoints.GET_DIARY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postFilterDTO)))
                .andExpect(status().isBadRequest());
        verify(postService, never()).getDiary(any(PostFilterDTO.class));
    }

    static Stream<Arguments> paginationOfDiaryPostsFailParameters() {
        return Stream.of(
                Arguments.of(0, 0, null, null, null),// page size must be more than 0
                Arguments.of(-1, 1, null, null, null), // page number must be at least 0
                Arguments.of(null, 4, null, null, null),// page number must not be null
                Arguments.of(4, null, null, null, null),// page size must not be null
                Arguments.of(null, null, null, null, null),// page number and size must not be null
                Arguments.of(0, 1, 0, null, null), // day must be bigger than 0
                Arguments.of(0, 1, -5, null, null),
                Arguments.of(0, 1, 32, null, null), // day must be less than or equal to 31
                Arguments.of(0, 1, 2, 0, null),// month must be bigger than 0
                Arguments.of(0, 1, 2, -1, null),
                Arguments.of(0, 1, null, 13, null)// month must be less than or equal to 12
        );
    }

    @ParameterizedTest
    @MethodSource("paginationOfDiaryPostsAcceptParameters")
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void getDiaryOfUserWithAcceptPagination(Integer pageNumber,
                                            Integer pageSize,
                                            Integer day,
                                            Integer month,
                                            Integer year) throws Exception {
        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(pageNumber).pageSize(pageSize).day(day).month(month).year(year).build();
        mockMvc.perform(post(EndPoints.POST + EndPoints.GET_DIARY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postFilterDTO)))
                .andExpect(status().isOk());
        verify(postService, times(1)).getDiary(any(PostFilterDTO.class));
    }

    static Stream<Arguments> paginationOfDiaryPostsAcceptParameters() {
        return Stream.of(
                Arguments.of(0, 1, null, null, null),
                Arguments.of(10, 10, null, null, null),
                Arguments.of(10, 10, 10, null, null),
                Arguments.of(10, 10, 10, 11, null),
                Arguments.of(10, 10, 10, 11, 2020),
                Arguments.of(10, 10, null, 11, 2020),
                Arguments.of(10, 10, null, null, 2020)
        );
    }

    @ParameterizedTest
    @MethodSource("feedWithFiltersTestParameter")
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void getFeedOfUserWithFeelingFilter(Integer pageNumber,
                                            Integer pageSize,
                                            List<Feeling> feelings,
                                            Integer expectedStatus) throws Exception {
        int numberOfMethodCall = expectedStatus==200?1:0;
        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(pageNumber).pageSize(pageSize).feelings(feelings).build();
        mockMvc.perform(post(EndPoints.POST + EndPoints.GET_FEED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postFilterDTO)))
                .andExpect(status().is(expectedStatus));
        verify(postService, times(numberOfMethodCall)).getFeed(any(PostFilterDTO.class));
    }

    static Stream<Arguments> feedWithFiltersTestParameter() {
        return Stream.of(
                Arguments.of(0, 1, null,HttpStatus.OK.value()),
                Arguments.of(0, 1, List.of(HAPPY),HttpStatus.OK.value()),
                Arguments.of(0, 1, List.of(HAPPY,SAD),HttpStatus.OK.value()),
                Arguments.of(0, 1, List.of(HAPPY,SAD,LOVE),HttpStatus.OK.value()),
                Arguments.of(5, 10, List.of(HAPPY,SAD,LOVE),HttpStatus.OK.value()),
                Arguments.of(0, 5, List.of(HAPPY,SAD,LOVE,INSPIRE),HttpStatus.OK.value()),
                Arguments.of(0, 5, List.of(HAPPY,SAD,LOVE,INSPIRE,ANXIOUS,FEAR),HttpStatus.OK.value()),
                Arguments.of(-1, 5, List.of(HAPPY,SAD,LOVE),HttpStatus.BAD_REQUEST.value()),
                Arguments.of(0, 0, List.of(HAPPY,SAD,LOVE),HttpStatus.BAD_REQUEST.value())
        );
    }

    @ParameterizedTest
    @MethodSource("paginationOfSavedPostsAcceptParameters")
    @WithMockUser(username = "greyUser", roles = "ROLES_USER")
    void getSavedPostsOfUserWithAcceptPagination(Integer pageNumber, Integer pageSize, Integer day, Integer month,
                                            Integer year, List<Feeling> feelings, Integer expectedStatus) throws Exception {
        int numberOfMethodCall = expectedStatus==200?1:0;
        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(pageNumber).pageSize(pageSize).day(day).month(month).year(year).feelings(feelings).build();
        mockMvc.perform(post(EndPoints.POST + EndPoints.GET_SAVED_POSTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postFilterDTO)))
                .andExpect(status().is(expectedStatus));
        verify(savedPostService, times(numberOfMethodCall)).getSavedPosts(any(PostFilterDTO.class));
    }

    static Stream<Arguments> paginationOfSavedPostsAcceptParameters() {
        return Stream.of(
                Arguments.of(0, 1, null, null, null, null, HttpStatus.OK.value()),
                Arguments.of(10, 10, null, null, null, null, HttpStatus.OK.value()),
                Arguments.of(10, 10, 10, null, null, null, HttpStatus.OK.value()),
                Arguments.of(10, 10, 10, 11, null, null, HttpStatus.OK.value()),
                Arguments.of(10, 10, 10, 11, 2020, null, HttpStatus.OK.value()),
                Arguments.of(10, 10, null, 11, 2020, null, HttpStatus.OK.value()),
                Arguments.of(10, 10, null, null, 2020, null, HttpStatus.OK.value()),

                Arguments.of(0, 1, null, null, null, null,HttpStatus.OK.value()),
                Arguments.of(0, 1, null, null, null, List.of(HAPPY),HttpStatus.OK.value()),
                Arguments.of(0, 1, null, null, null, List.of(HAPPY,SAD),HttpStatus.OK.value()),
                Arguments.of(0, 1, null, null, null, List.of(HAPPY,SAD,LOVE),HttpStatus.OK.value()),
                Arguments.of(5, 10, null, null, null, List.of(HAPPY,SAD,LOVE),HttpStatus.OK.value()),
                Arguments.of(0, 5, null, null, null, List.of(HAPPY,SAD,LOVE,INSPIRE),HttpStatus.OK.value()),
                Arguments.of(0, 5, null, null, null, List.of(HAPPY,SAD,LOVE,INSPIRE,ANXIOUS,FEAR),HttpStatus.OK.value()),
                Arguments.of(-1, 5, null, null, null, List.of(HAPPY,SAD,LOVE),HttpStatus.BAD_REQUEST.value()),
                Arguments.of(0, 0, null, null, null, List.of(HAPPY,SAD,LOVE),HttpStatus.BAD_REQUEST.value())
        );
    }
}