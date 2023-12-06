package com.software.grey;

import com.software.grey.controllers.TestController;
import com.software.grey.models.dtos.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.software.grey.utils.EndPoints.SIGNUP;
import static com.software.grey.utils.EndPoints.TEST;
import static com.software.grey.utils.JsonUtil.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WebMvcTest(TestController.class)
class AuthorizationTest {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(username = "greyadmin", roles = "ROLES_ADMIN")
    @Test
    void givenAdminRequestOnTestEndpoint_ShouldSucceed() throws Exception {
        mockMvc.perform(get(TEST))
                .andExpect(status().isOk());
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }
}
