package com.software.grey.TestDataUtil;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ObjectsBuilder {

    public User createUserA() {
        return User.builder()
                .email("abdulrahman@gmail.com")
                .username("abdulrahman")
                .build();
    }

    public User createUserB() {
        return User.builder()
                .email("h@gmail.com")
                .username("7a7a")
                .build();
    }

    public Post createPostA(User user) {
        return Post.builder()
                .postText("new post aa")
                .user(user)
                .build();
    }

    public Post createPostB(User user) {
        return Post.builder()
                .postText("new post bb")
                .user(user)
                .build();
    }
}
