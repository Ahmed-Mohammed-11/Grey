//package com.software.grey.EndPointTest;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.software.grey.TestDataUtil.ObjectsBuilder;
//import com.software.grey.controllers.SignupController;
//import com.software.grey.models.dtos.UserDTO;
//import com.software.grey.models.entities.BasicUser;
//import com.software.grey.models.entities.Post;
//import com.software.grey.models.entities.User;
//import com.software.grey.repositories.BasicUserRepo;
//import com.software.grey.repositories.PostRepository;
//import com.software.grey.repositories.UserRepo;
//import com.software.grey.utils.SecurityUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static com.software.grey.utils.EndPoints.SIGNUP;
//import static com.software.grey.utils.JsonUtil.asJsonString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@AutoConfigureMockMvc
//class TestSavedPostServiceEndPoint {
//    private final MockMvc mockMvc;
//    private final UserRepo userRepository;
//    private final PostRepository postRepository;
//    private final ObjectsBuilder objectsBuilder;
//    private final SecurityUtils securityUtils;
//    private final SignupController signupController;
//
//    @Autowired
//    private BasicUserRepo basicUserRepo;
//    ObjectMapper objectMapper;
//
//    @Autowired
//    public TestSavedPostServiceEndPoint(MockMvc mockMvc,
//                                        UserRepo userRepository, PostRepository postRepository,
//                                        ObjectsBuilder objectsBuilder, SecurityUtils securityUtils, SignupController signupController) {
//        this.mockMvc = mockMvc;
//        this.postRepository = postRepository;
//        this.userRepository = userRepository;
//        this.objectsBuilder = objectsBuilder;
//        this.objectMapper = new ObjectMapper();
//        this.securityUtils = securityUtils;
//        this.signupController = signupController;
//    }
//
//    @Test
//    void testEndPointWithValidData() throws Exception{
//        UserDTO myUser = new UserDTO("mockEmail12@gmail.com", "testUser", "mock Password test");
//        mockMvc.perform(post(SIGNUP)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(myUser)));
//
//        User b = objectsBuilder.createUserB();
//        userRepository.save(b);
//
//        // create post
//        Post postB = objectsBuilder.createPostB(b);
//        postRepository.save(postB);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/post/toggle/save/" + postB.getId())
//        ).andExpect(
//                MockMvcResultMatchers.status().isOk()
//        );
//    }
//}
