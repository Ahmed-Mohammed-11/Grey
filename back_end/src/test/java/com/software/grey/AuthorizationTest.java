package com.software.grey;

import com.software.grey.services.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.software.grey.utils.EndPoints.POST;
import static com.software.grey.utils.EndPoints.REPORT_POST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    @WithMockUser(username = "greyUser", authorities = "MODERATOR")
    void givenAdminRequestOnTestEndpoint_ShouldSucceed() throws Exception {
        when(postService.getReportedPosts(any())).thenReturn(null);
        mockMvc.perform(post(POST + REPORT_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postText":"this is the mock post text",
                                "postFeelings":["LOVE"]
                                }"""))
                .andExpect(status().isOk());
        verify(postService, times(1)).getReportedPosts(any());
    }

    @Test
    void notGivenAdminRequestOnTestEndpoint_ShouldFail() throws Exception {
        when(postService.getReportedPosts(any())).thenReturn(null);
        mockMvc.perform(post(POST + REPORT_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postText":"this is the mock post text",
                                "postFeelings":["LOVE"]
                                }"""))
                .andExpect(status().isUnauthorized());
        verify(postService, times(0)).getReportedPosts(any());
    }
}
