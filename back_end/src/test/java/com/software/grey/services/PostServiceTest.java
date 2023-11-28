package com.software.grey.services;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.software.grey.models.enums.Feeling.HAPPY;
import static com.software.grey.models.enums.Feeling.LOVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepo userRepo;

    @MockBean
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @AfterEach
    void cleanUp() {
        postRepository.deleteAll();
        userRepo.deleteAll();
    }
    @Test
    void addPostCorrectly(){

        PostDTO postDTO = PostDTO.builder()
                .postText("this is a mocked text")
                .postFeelings(Set.of(LOVE, HAPPY)).build();

        UserDTO userDTO = new UserDTO("mockEmail11@gmail.com", "mockedUserName11","mockPas11");

        userService.save(userDTO);

        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName");

        UUID postId = postService.add(postDTO);

        assertThat(postId).isNotNull();
        assertThat(postRepository.existsById(postId)).isTrue();
        Post retrievedPost = postService.findPostById(postId);
        assertThat(retrievedPost.getPostFeelings()).hasSize(2).contains(LOVE, HAPPY);

        Duration difference = Duration.between(retrievedPost.getPostTime().toInstant(), Instant.now());
        assertThat(difference).isLessThan(Duration.ofMinutes(1));
    }

    void prepareDataUser1() throws InterruptedException {
        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName1");
        UserDTO userDTO1 = new UserDTO("mockEmail1@gmail.com", "mockedUserName1","mockPas1");
        userService.save(userDTO1);
        for(int i = 0;i<5;i++){
            postService.add(PostDTO.builder().postText(i + " user1").postFeelings(Set.of(LOVE, HAPPY)).build());
            Thread.sleep(30);
        }
    }

    void prepareDataUser2() throws InterruptedException {
        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName2");
        UserDTO userDTO2 = new UserDTO("mockEmail2@gmail.com", "mockedUserName2","mockPas2");
        userService.save(userDTO2);
        for(int i = 0;i<3;i++){
            postService.add(PostDTO.builder().postText(i + " user2").postFeelings(Set.of(LOVE, HAPPY)).build());
            Thread.sleep(30);
        }
    }

    //TODO: you should remove the population of the DB form the test function
    @ParameterizedTest
    @MethodSource("paginationOfDiaryPostsParameters")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getDiaryOfUser1(String userName, Integer pageSize, Integer pageNumber, Integer day, Integer month,
                         Integer year, Integer contentSize) throws InterruptedException {
        prepareDataUser1();
        prepareDataUser2();

        Map<String, List<String>> userPosts = Map.of(
                "mockedUserName1", List.of("4 user1", "3 user1", "2 user1", "1 user1", "0 user1"),
                "mockedUserName2", List.of("2 user2", "1 user2", "0 user2")
        );

        when(securityUtils.getCurrentUserName()).thenReturn(userName);

        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(pageNumber).pageSize(pageSize).day(day).month(month).year(year).build();
        Page<PostDTO> posts = postService.getAll(postFilterDTO);
        assertThat(posts.getContent()).hasSize(contentSize);
        int start = pageNumber * pageSize;
        for (int i = 0; i < contentSize; i++) {
            assertThat(posts.getContent().get(i).getPostText()).isEqualTo(userPosts.get(userName).get(i + start));
        }
    }


    static Stream<Arguments> paginationOfDiaryPostsParameters() {
        LocalDate currentDate = LocalDate.now();

        // Get the day, month, and year
        int day = currentDate.getDayOfMonth();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();
        return Stream.of(
                Arguments.of("mockedUserName1", 1, 0, day, month, year, 1), // check the user1 has at least one post
                Arguments.of("mockedUserName2", 1, 0, day, month, year, 1), // check the user2 has at least one post
                Arguments.of("mockedUserName1", 10, 0, day, month, year, 5), // check all the posts of user1
                Arguments.of("mockedUserName2", 10, 0, day, month, year, 3), // check all the posts of user2
                Arguments.of("mockedUserName1", 2, 1, day, month, year, 2), // check posts of page 2 of user1
                Arguments.of("mockedUserName2", 2, 1, day, month, year, 1), // check posts of page 2 of user2
                Arguments.of("mockedUserName1", 10, 0, (day + 5) % 30, month, year, 0), // check posts of user that created in other day
                Arguments.of("mockedUserName2", 10, 0, (day + 5) % 30, month, year, 0)
        );
    }
}
