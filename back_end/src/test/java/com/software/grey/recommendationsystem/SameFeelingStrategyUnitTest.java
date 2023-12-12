package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.services.implementations.PostService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class SameFeelingStrategyUnitTest {
    @Mock
    private PostService postService;
    @Autowired
    @InjectMocks
    private SameFeelingStrat sameFeelingStrat;

    @SpyBean
    private PostRepository postRepository;

    @Autowired
    private UserRepo userRepo;
    private User user1;
    private User user2;

    @BeforeAll
    void init() {
        user1 = User.builder().email("sameFeelingUnit@example.com").username("sameFeelingUnit").build();
        user2 = User.builder().email("sameFeelingUnit2@example.com").username("sameFeelingUnit2").build();
        userRepo.save(user1);
        userRepo.save(user2);
        for(int i = 0; i < 30; i++) {
            Set<Feeling> set = new TreeSet<>();
            set.add(Feeling.SAD);
            Post post = Post.builder().postText("Sad").user(user2).postFeelings(set).build();
            postRepository.save(post);
        }
        for(int i = 0; i < 30; i++) {
            Set<Feeling> set = new TreeSet<>();
            set.add(Feeling.ANXIOUS);
            Post post = Post.builder().postText("Anxious").user(user2).postFeelings(set).build();
            postRepository.save(post);
        }
    }

    @AfterAll
    void del(){
        postRepository.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void recommend() {
        List<FeelingCountProjection> myList = new ArrayList<>();
        FeelingCountProjection feelingCountProjection = new FeelingCountImpl();
        feelingCountProjection.setFeeling(Feeling.SAD);
        feelingCountProjection.setFeelingCount(1);
        myList.add(feelingCountProjection);
        Map<Feeling, Double> feelingPercentage = new LinkedHashMap<>();
        feelingPercentage.put(Feeling.SAD, 1.0);
        Pageable page = PageRequest.of(0, 10);
        when(postService.getCountOfPostedFeelings(user1)).thenReturn(myList);
//        when(sameFeelingStratSpy.getFeelingPercentage(myList)).thenReturn(feelingPercentage);
//        when(postRepository.findByPostFeelings(Feeling.SAD, page)).thenReturn()

        List<Post> returnedData = sameFeelingStrat.recommend(user1, 0, 10);

        assertEquals(10, returnedData.size());
        for(Post p : returnedData) {
            assertTrue(p.getPostFeelings().contains(Feeling.SAD));
        }
    }

    @Test
    void recommendWithEmptyData() {
        // Test when getCountOfPostedFeelings returns an empty list

        // Mock the behavior of dependencies
        when(postService.getCountOfPostedFeelings(user1)).thenReturn(Collections.emptyList());

        // Call the method under test
        List<Post> returnedData = sameFeelingStrat.recommend(user1, 0, 10);

        // Assertions
        assertTrue(returnedData.isEmpty(), "The result should be an empty list");
    }

    @Test
    void recommendWithNoFeelingPercentage() {
        // Test when getFeelingPercentage returns an empty map
        FeelingCountProjection feelingCountProjection = new FeelingCountImpl();
        feelingCountProjection.setFeeling(Feeling.SAD);
        feelingCountProjection.setFeelingCount(0);
        List<FeelingCountProjection> myList = new ArrayList<>();
        myList.add(feelingCountProjection);
        // Mock the behavior of dependencies
        when(postService.getCountOfPostedFeelings(user1)).thenReturn(myList);
//        when(sameFeelingStratSpy.getFeelingPercentage(myList)).thenReturn(Collections.emptyMap());

        assertTrue(sameFeelingStrat.recommend(user1, 0, 10).size() == 0);
    }

    @Test
    void recommendWithManyFeelings_ButNoPostsAvailable() {
        // Test when there is more data in the database
        FeelingCountProjection feelingCountProjection = new FeelingCountImpl();
        feelingCountProjection.setFeeling(Feeling.HAPPY);
        feelingCountProjection.setFeelingCount(30);
        List<FeelingCountProjection> myList = new ArrayList<>();
        myList.add(feelingCountProjection);
        feelingCountProjection.setFeeling(Feeling.INSPIRE);
        feelingCountProjection.setFeelingCount(30);
        myList.add(feelingCountProjection);


        // Mock the behavior of dependencies
        when(postService.getCountOfPostedFeelings(user2)).thenReturn(myList);
//        when(sameFeelingStratSpy.getFeelingPercentage(myList).thenReturn(Collections.singletonMap(Feeling.HAPPY, 1.0));

        // Call the method under test
        List<Post> returnedData = sameFeelingStrat.recommend(user2, 0, 50);

        // Assertions
        assertEquals(0, returnedData.size(), "The result should contain 0 posts");
    }

    @Test
    void recommendWithManyFeelings_ManyPostsAvailable() {
        // Test when there is more data in the database
        FeelingCountProjection feelingCountProjection = new FeelingCountImpl();
        feelingCountProjection.setFeeling(Feeling.SAD);
        feelingCountProjection.setFeelingCount(25);
        List<FeelingCountProjection> myList = new ArrayList<>();
        myList.add(feelingCountProjection);
        FeelingCountProjection feelingCountProjection2 = new FeelingCountImpl();
        feelingCountProjection2.setFeeling(Feeling.ANXIOUS);
        feelingCountProjection2.setFeelingCount(25);
        myList.add(feelingCountProjection2);
        Map<Feeling, Double> map = new LinkedHashMap<>();
        map.put(Feeling.SAD, 0.5);
        map.put(Feeling.ANXIOUS, 0.5);

        // Mock the behavior of dependencies
        when(postService.getCountOfPostedFeelings(user2)).thenReturn(myList);

//        when(sameFeelingStratSpy.getFeelingPercentage(myList)).thenReturn(map);
        // Call the method under test
        List<Post> returnedData = sameFeelingStrat.recommend(user2, 0, 50);

        // Assertions
        assertEquals(50, returnedData.size(), "The result should contain 50 posts");
    }

    private FeelingCountProjection createFeelingCountProjection() {
        FeelingCountProjection feelingCountProjection = new FeelingCountImpl();
        feelingCountProjection.setFeeling(Feeling.SAD);
        feelingCountProjection.setFeelingCount(1);
        return feelingCountProjection;
    }


}