package com.software.grey.services;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.mappers.UserMapper;
import com.software.grey.repositories.BasicUserRepo;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.UserRepo;
import com.software.grey.repositories.UserVerificationRepo;
import com.software.grey.services.implementations.PostServiceImpl;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import java.util.stream.Stream;

import static com.software.grey.models.enums.Feeling.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
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
    private PostServiceImpl postService;

    @Autowired
    private BasicUserRepo basicUserRepo;
    @Autowired
    private UserVerificationRepo userVerificationRepo;

    @Autowired
    private UserMapper userMapper;

    @BeforeAll
    void init() throws InterruptedException {
        prepareDataUser1();
        prepareDataUser2();
    }

    @AfterAll
    void del() {
        postRepository.deleteAll();
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepo.deleteAll();
    }

    void prepareDataUser1() throws InterruptedException {
        UserDTO userDTO1 = new UserDTO("PostServiceUsername1@gmail.com", "PostServiceUsername1", "mockPas1");
        userService.save(userDTO1);
        when(securityUtils.getCurrentUser()).thenReturn(userService.findByUserName(userDTO1.username));
        List<Set<Feeling>> feelings = List.of(Set.of(LOVE), Set.of(LOVE, HAPPY), Set.of(SAD), Set.of(LOVE, HAPPY, SAD));
        for (int i = 0; i < 5; i++) {
            postService.add(PostDTO.builder().postText(i + " user1").postFeelings(feelings.get(i % feelings.size())).build());
            Thread.sleep(30);
        }
    }

    void prepareDataUser2() throws InterruptedException {
        UserDTO userDTO2 = new UserDTO("PostServiceUsername2@gmail.com", "PostServiceUsername2", "mockPas2");
        userService.save(userDTO2);
        when(securityUtils.getCurrentUser()).thenReturn(userService.findByUserName(userDTO2.username));
        List<Set<Feeling>> feelings = List.of(Set.of(LOVE), Set.of(SAD));
        for (int i = 0; i < 3; i++) {
            postService.add(PostDTO.builder().postText(i + " user2").postFeelings(feelings.get(i % feelings.size())).build());
            Thread.sleep(30);
        }
    }

    @Test
    void addPostCorrectly() {

        PostDTO postDTO = PostDTO.builder()
                .postText("this is a mocked text")
                .postFeelings(Set.of(LOVE, HAPPY)).build();

        UserDTO userDTO = new UserDTO("mockEmail11@gmail.com", "PostServiceUsername11", "mockPas11");

        userService.save(userDTO);

        when(securityUtils.getCurrentUserName()).thenReturn("mockedUserName");

        String postId = postService.add(postDTO);

        assertThat(postId).isNotNull();
        assertThat(postRepository.existsById(postId)).isTrue();
        Post retrievedPost = postService.findPostById(postId);
        assertThat(retrievedPost.getPostFeelings()).hasSize(2).contains(LOVE, HAPPY);

        Duration difference = Duration.between(retrievedPost.getPostTime().toInstant(), Instant.now());
        assertThat(difference).isLessThan(Duration.ofMinutes(1));
    }

    @ParameterizedTest
    @MethodSource("paginationOfDiaryPostsParameters")
    void getDiaryOfUser1(String userName, Integer pageSize, Integer pageNumber, Integer day, Integer month,
                         Integer year, Integer contentSize) throws InterruptedException {

        Map<String, List<String>> userPosts = Map.of(
                "PostServiceUsername1", List.of("4 user1", "3 user1", "2 user1", "1 user1", "0 user1"),
                "PostServiceUsername2", List.of("2 user2", "1 user2", "0 user2")
        );

        when(securityUtils.getCurrentUserName()).thenReturn(userName);

        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(pageNumber).pageSize(pageSize).day(day).month(month).year(year).build();
        Page<PostDTO> posts = postService.getDiary(postFilterDTO);
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
                Arguments.of("PostServiceUsername1", 1, 0, day, month, year, 1), // check the user1 has at least one post
                Arguments.of("PostServiceUsername2", 1, 0, day, month, year, 1), // check the user2 has at least one post
                Arguments.of("PostServiceUsername1", 10, 0, day, month, year, 5), // check all the posts of user1
                Arguments.of("PostServiceUsername2", 10, 0, day, month, year, 3), // check all the posts of user2
                Arguments.of("PostServiceUsername1", 2, 1, day, month, year, 2), // check posts of page 2 of user1
                Arguments.of("PostServiceUsername2", 2, 1, day, month, year, 1), // check posts of page 2 of user2
                Arguments.of("PostServiceUsername1", 10, 0, (day + 5) % 30, month, year, 0), // check posts of user that created in other day
                Arguments.of("PostServiceUsername2", 10, 0, (day + 5) % 30, month, year, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("getFeedOfUserTestData")
    void getFeedOfUser(String userName, Integer pageNumber, Integer pageSize, List<Feeling> feelings
            , List<String> postsStrings) throws InterruptedException {

//        User user = User.builder().username(userName).build();
//        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(securityUtils.getCurrentUserName()).thenReturn(userName);

        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(pageNumber).pageSize(pageSize).feelings(feelings).build();

        Page<PostDTO> posts = postService.getFeed(postFilterDTO);

        assertThat(posts.getContent()).hasSize(postsStrings.size());

        int start = pageNumber * pageSize;
        for (int i = 0; i < postsStrings.size(); i++) {
            assertThat(posts.getContent().get(i).getPostText()).isEqualTo(postsStrings.get(i + start));
        }
    }

    static Stream<Arguments> getFeedOfUserTestData() {
        return Stream.of(
                Arguments.of("PostServiceUsername1", 0, 10, null, List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("PostServiceUsername2", 0, 10, null, List.of("4 user1", "3 user1", "2 user1", "1 user1", "0 user1")),
                Arguments.of("PostServiceUsername1", 0, 10, List.of(LOVE), List.of("2 user2", "0 user2")),
                Arguments.of("PostServiceUsername2", 0, 10, List.of(LOVE, HAPPY), List.of("4 user1", "3 user1", "1 user1", "0 user1")),
                Arguments.of("PostServiceUsername1", 0, 10, List.of(LOVE, HAPPY, SAD), List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("PostServiceUsername1", 0, 10, List.of(LOVE, HAPPY, SAD, INSPIRE), List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("PostServiceUsername2", 0, 10, List.of(SAD), List.of("3 user1", "2 user1")),
                Arguments.of("PostServiceUsername2", 0, 10, List.of(INSPIRE), List.of()),// return noting
                Arguments.of("PostServiceUsername2", 0, 10, List.of(), List.of("4 user1", "3 user1", "2 user1", "1 user1", "0 user1"))// return all
        );
    }

}
