package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;
import com.software.grey.models.enums.Feeling;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CombinerImpTest {
    @Autowired
    CombinerImp combiner;
    @Test
    void smokeTestCombine() {
        List<List<Post>> posts = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            List<Post> temp = new ArrayList<>();
            for(int j = 0; j < 20; j++) {
                Post post = Post.builder()
                        .id(UUID.randomUUID())
                        .postText("tired " + j)
                        .postFeelings(Collections.singleton(Feeling.SAD))
                        .postTime(new Timestamp(System.currentTimeMillis() + (j+1)*(i+1)))
                        .build();
                temp.add(post);
            }
            posts.add(temp);
        }

        List<Post> ret = combiner.combine(posts);

        for (int i = 0; i < ret.size() - 1; i++) {
            assertTrue(ret.get(i).getPostTime().after(ret.get(i + 1).getPostTime()) ||
                    ret.get(i).getPostTime().equals(ret.get(i + 1).getPostTime()));
        }
    }

    @Test
    void testCombineWithEmptyLists() {
        // Test when input lists are empty
        List<List<Post>> posts = new ArrayList<>();
        List<Post> ret = combiner.combine(posts);
        assertTrue(ret.isEmpty(), "Result should be an empty list");
    }

    @Test
    void testCombineWithSingleList() {
        // Test when there is only one list of posts
        List<List<Post>> posts = new ArrayList<>();
        List<Post> singleList = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            Post post = Post.builder()
                    .id(UUID.randomUUID())
                    .postText("happy " + j)
                    .postFeelings(Collections.singleton(Feeling.HAPPY))
                    .postTime(new Timestamp(System.currentTimeMillis() + j + 1))
                    .build();
            singleList.add(post);
        }
        posts.add(singleList);

        List<Post> ret = combiner.combine(posts);
        Collections.reverse(singleList);
        assertEquals(singleList, ret, "Result should be the same as the single list");
    }
}