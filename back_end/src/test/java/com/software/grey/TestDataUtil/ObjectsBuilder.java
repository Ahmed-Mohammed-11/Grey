package com.software.grey.TestDataUtil;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ObjectsBuilder {

    public User createUserA() {
        User userA = User.builder()
                .email("abdulrahman@gmail.com")
                .password("sha256jj")
                .username("abdulrahman")
                .build();
        return userA;
    }

    public User createUserB() {
        User userA = User.builder()
                .email("h@gmail.com")
                .password("xxxxxx")
                .username("7a7a")
                .build();
        return userA;
    }

    public User createUserC() {
        User userA = User.builder()
                .email("hamada@gmail.com")
                .password("Manga")
                .username("Eren eager")
                .build();
        return userA;
    }
    public Post createPostA(User user) {
        Post post = Post.builder()
                .text("new post aa")
                .user(user)
                .build();
        return post;
    }

    public Post createPostB(User user) {
        Post post = Post.builder()
                .text("new post bb")
                .user(user)
                .build();
        return post;
    }
}
