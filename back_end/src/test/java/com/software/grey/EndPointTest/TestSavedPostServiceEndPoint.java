package com.software.grey.EndPointTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.grey.TestDataUtil.ObjectsBuilder;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class TestSavedPostServiceEndPoint {
    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ObjectsBuilder objectsBuilder;
    ObjectMapper objectMapper;

    @Autowired
    public TestSavedPostServiceEndPoint(MockMvc mockMvc,
                                        UserRepository userRepository, PostRepository postRepository,
                                        ObjectsBuilder objectsBuilder) {
        this.mockMvc = mockMvc;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.objectsBuilder = objectsBuilder;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testEndPointWithValidData() throws Exception{
        User a = objectsBuilder.createUserA();
        User b = objectsBuilder.createUserB();
        userRepository.save(a);
        userRepository.save(b);

        // create posts and save them
        Post postA = objectsBuilder.createPostA(a);
        Post postB = objectsBuilder.createPostB(b);
        postRepository.save(postA);
        postRepository.save(postB);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/post/toggle/save/" + postB.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
}
