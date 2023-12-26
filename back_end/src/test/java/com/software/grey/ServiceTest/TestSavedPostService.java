package com.software.grey.ServiceTest;


import com.software.grey.SavedPostEnum;
import com.software.grey.TestDataUtil.ObjectsBuilder;
import com.software.grey.controllers.SignupController;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.repositories.*;
import com.software.grey.services.SavedPostService;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.SecurityUtils;
import lombok.AllArgsConstructor;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Filter;
import java.util.stream.Stream;

import static com.software.grey.models.enums.Feeling.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestSavedPostService {

    @MockBean
    private SecurityUtils securityUtils;
    private final SavedPostService savedPostService;
    private final ObjectsBuilder objectsBuilder;
    private final UserRepo userRepository;
    private final PostRepository postRepository;
    private final SignupController signupController;
    private final SavedPostRepository savedPostRepository;
    private final UserVerificationRepo userVerificationRepo;
    private final BasicUserRepo basicUserRepo;
    private final PostService postService;

    @Autowired
    TestSavedPostService (SavedPostService savedPostService, ObjectsBuilder objectsBuilder1,
                          UserRepo userRepository, PostRepository postRepository,
                          SavedPostRepository savedPostRepository, SignupController signupController,
                          UserVerificationRepo userVerificationRepo, BasicUserRepo basicUserRepo, PostService postService) {
        this.savedPostService = savedPostService;
        this.objectsBuilder = objectsBuilder1;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.savedPostRepository = savedPostRepository;
        this.signupController = signupController;
        this.userVerificationRepo = userVerificationRepo;
        this.basicUserRepo = basicUserRepo;
        this.postService = postService;
    }

    @BeforeAll
    void init() throws InterruptedException {
        UserDTO myUser = new UserDTO("mockEmailSave@gmail.com", "testUserSave", "mock Password test");
        signupController.signup(myUser);


        UserDTO myUser2 = new UserDTO("b.mail@gmail.com", "userB", "Omar Tammam");
        signupController.signup(myUser2);

        User b = userRepository.findByUsername("userB");
        Post postB = objectsBuilder.createPostB(b);
        postRepository.save(postB);
        prepareDataUser1();
        prepareDataUser2();

    }

    @AfterAll
    void cleanUp() {
        postRepository.deleteAll();
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepository.deleteAll();
    }

    void prepareDataUser1() throws InterruptedException {
        when(securityUtils.getCurrentUserName()).thenReturn("testUserSave");
        List<Set<Feeling>> feelings = List.of(Set.of(LOVE), Set.of(LOVE, HAPPY), Set.of(SAD), Set.of(LOVE, HAPPY, SAD));
        for(int i = 0;i<5;i++){
            postService.add(PostDTO.builder().postText(i + " user1").postFeelings(feelings.get(i% feelings.size())).build());
            Thread.sleep(30);
        }
    }

    void prepareDataUser2() throws InterruptedException {
        when(securityUtils.getCurrentUserName()).thenReturn("userB");
        List<Set<Feeling>> feelings = List.of(Set.of(LOVE), Set.of(SAD));
        for(int i = 0;i<3;i++){
            postService.add(PostDTO.builder().postText(i + " user2").postFeelings(feelings.get(i% feelings.size())).build());
            Thread.sleep(30);
        }
    }

    @Test
    void testSavePostWithValidData() {
        User a = userRepository.findByUsername("testUserSave");
        User b = userRepository.findByUsername("userB");
        Post postB = postRepository.findByUser(b);
        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);
        // save the post and assert that it saved successfully
        SavedPostEnum savedPostEnum = savedPostService.toggleSavedPost(postB.getId().toString());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.SAVED);

        // get all saved posts
        Iterable<SavedPost> savedPost = savedPostRepository.findAll();
        assertThat(savedPost).hasSize(1);

        // check the retrieved data
        SavedPost s = savedPost.iterator().next();

        // assert post data
        Post retrievedPost = s.getPost();
        assertThat(retrievedPost.getId()).isEqualTo(postB.getId());
        assertThat(retrievedPost.getUser().getUsername()).isEqualTo(b.getUsername());
        assertThat(retrievedPost.getPostText()).isEqualTo(postB.getPostText());

        // assert user data
        User retrievedUser = s.getUser();
        assertThat(retrievedUser.getId()).isEqualTo(a.getId());
        assertThat(retrievedUser.getEmail()).isEqualTo(a.getEmail());
        assertThat(retrievedUser.getUsername()).isEqualTo(a.getUsername());
        savedPostRepository.deleteAll();
    }

    @Test
    void testUnSavePostWithValidData() {
        User a = userRepository.findByUsername("testUserSave");
        User b = userRepository.findByUsername("userB");
        Post postB = postRepository.findByUser(b);
        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);
        // save the post and assert that it saved successfully
        SavedPostEnum savedPostEnum = savedPostService.toggleSavedPost(postB.getId().toString());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.SAVED);

        // assert that the post is saved
        Iterable<SavedPost> savedPost = savedPostRepository.findAll();
        assertThat(savedPost).hasSize(1);

        // un-save the post
        savedPostEnum = savedPostService.toggleSavedPost(postB.getId().toString());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.REMOVED);

        savedPost = savedPostRepository.findAll();
        assertThat(savedPost).isEmpty();

        // assert that user a and postB are not removed
        Optional<User> user = userRepository.findById(a.getId());
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(a.getUsername());

        Optional<Post> post = postRepository.findById(postB.getId());
        assertThat(post).isPresent();
        assertThat(post.get().getPostText()).isEqualTo(postB.getPostText());
        savedPostRepository.deleteAll();
    }

    @Test
    void testSavedPostServiceWithInvalidPost() {
        // create user that will save the post
        User a = userRepository.findByUsername("testUser");

        // create user to be the post author
        User b = userRepository.findByUsername("userB");

        // Mock the securityUtils method
        when(securityUtils.getCurrentUser()).thenReturn(a);

        // create the post but do not save it, so it will be not-found
        Post post = postRepository.findByUser(b);
        UUID postId = post.getId();
        postRepository.delete(post);

        SavedPostEnum savedPostEnum = savedPostService.toggleSavedPost(postId.toString());
        assertThat(savedPostEnum).isEqualTo(SavedPostEnum.NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getSavedPostsOfUserTestData")
    void getSavedPostsOfUser(String userName, Integer pageNumber, Integer pageSize,Integer day, Integer month,
                             Integer year, List<Feeling> feelings, List<String> postsStrings) throws InterruptedException {

        when(securityUtils.getCurrentUserName()).thenReturn(userName);

        PostFilterDTO postFilterDTO = PostFilterDTO.builder()
                .pageNumber(pageNumber).pageSize(pageSize).day(day).month(month).year(year).feelings(feelings).build();
        Page<PostDTO> posts = savedPostService.getSavedPosts(postFilterDTO);
        assertThat(posts.getContent()).hasSize(postsStrings.size());
        int start = pageNumber * pageSize;
        for (int i = 0; i < postsStrings.size(); i++) {
            assertThat(posts.getContent().get(i).getPostText()).isEqualTo(postsStrings.get(i + start));
        }
    }

    static Stream<Arguments> getSavedPostsOfUserTestData() {
        return Stream.of(
                Arguments.of("testUserSave", 0, 10, null, null, null, null, List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("userB", 0, 10, null, null, null, null, List.of("4 user1", "3 user1", "2 user1", "1 user1", "0 user1")),
                Arguments.of("testUserSave", 0, 10, null, null, null, List.of(LOVE), List.of("2 user2", "0 user2")),
                Arguments.of("userB", 0, 10, null, null, null, List.of(LOVE, HAPPY), List.of("4 user1", "3 user1", "1 user1", "0 user1")),
                Arguments.of("testUserSave", 0, 10, null, null, null, List.of(LOVE, HAPPY, SAD),List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("testUserSave", 0, 10, null, null, null, List.of(LOVE, HAPPY, SAD, INSPIRE),List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("userB", 0, 10, null, null, null, List.of(SAD),List.of("3 user1", "2 user1")),
                Arguments.of("userB", 0, 10, null, null, null, List.of(INSPIRE),List.of()),// return noting
                Arguments.of("userB", 0, 10, null, null, null, List.of(), List.of("4 user1", "3 user1", "2 user1", "1 user1", "0 user1"))// return all
        );
    }

//    static Stream<Arguments> paginationOfDiaryPostsParameters() {
//        LocalDate currentDate = LocalDate.now();
//
//        // Get the day, month, and year
//        int day = currentDate.getDayOfMonth();
//        int month = currentDate.getMonthValue();
//        int year = currentDate.getYear();
//        return Stream.of(
//                Arguments.of("testUserSave", 1, 0, day, month, year, 1), // check the user1 has at least one post
//                Arguments.of("userB", 1, 0, day, month, year, 1), // check the user2 has at least one post
//                Arguments.of("testUserSave", 10, 0, day, month, year, 5), // check all the posts of user1
//                Arguments.of("userB", 10, 0, day, month, year, 3), // check all the posts of user2
//                Arguments.of("testUserSave", 2, 1, day, month, year, 2), // check posts of page 2 of user1
//                Arguments.of("userB", 2, 1, day, month, year, 1), // check posts of page 2 of user2
//                Arguments.of("testUserSave", 10, 0, (day + 5) % 30, month, year, 0), // check posts of user that created in other day
//                Arguments.of("userB", 10, 0, (day + 5) % 30, month, year, 0)
//        );
//    }
}