package com.software.grey.ServiceTest;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.enums.Feeling;
import com.software.grey.repositories.*;
import com.software.grey.services.SavedPostService;
import com.software.grey.services.UserService;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.software.grey.models.enums.Feeling.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FilterSavedPostTest {

    @MockBean
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private SavedPostService savedPostService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BasicUserRepo basicUserRepo;

    @Autowired
    private SavedPostRepository savedPostRepository;

    @Autowired
    private UserVerificationRepo userVerificationRepo;

    @BeforeAll
    void init() throws InterruptedException {

        List<String> postsIdUser1 = prepareDataUser1();
        List<String> postsIdUser2 = prepareDataUser2();

        initializeSavedPosts(postsIdUser1, "PostServiceUsername2");
        initializeSavedPosts(postsIdUser2, "PostServiceUsername1");
    }

    @AfterAll
    void del() {
        savedPostRepository.deleteAll();
        postRepository.deleteAll();
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        userRepo.deleteAll();
    }

    List<String> prepareDataUser1() throws InterruptedException {
        UserDTO userDTO1 = UserDTO.builder().username("PostServiceUsername1").email("PostServiceUsername1@gmail.com").password("111 222 333").build();
        userService.save(userDTO1);

        List<String> ids = new ArrayList<>();
        when(securityUtils.getCurrentUserName()).thenReturn("PostServiceUsername1");
        List<Set<Feeling>> feelings = List.of(Set.of(LOVE), Set.of(LOVE, HAPPY), Set.of(SAD), Set.of(LOVE, HAPPY, SAD));
        for(int i = 0;i<5;i++){
            String id = postService.add(PostDTO.builder().postText(i + " user1").postFeelings(feelings.get(i% feelings.size())).build());
            ids.add(id);
        }
        return ids;
    }

    List<String> prepareDataUser2() throws InterruptedException {
        UserDTO userDTO2 = UserDTO.builder().username("PostServiceUsername2").email("PostServiceUsername2@gmail.com").password("222 333 444").build();
        userService.save(userDTO2);

        List<String> ids = new ArrayList<>();
        when(securityUtils.getCurrentUserName()).thenReturn("PostServiceUsername2");
        List<Set<Feeling>> feelings = List.of(Set.of(LOVE), Set.of(SAD));
        for(int i = 0;i<3;i++){
            String id = postService.add(PostDTO.builder().postText(i + " user2").postFeelings(feelings.get(i% feelings.size())).build());
            Thread.sleep(30);
            ids.add(id);
        }
        return ids;
    }

    private void initializeSavedPosts(List<String> postIds, String username) throws InterruptedException {
        when(securityUtils.getCurrentUser()).thenReturn(userService.findByUserName(username));
        for (int i = 0; i < 3; i++) {
            String id = postIds.get(i);
            savedPostService.toggleSavedPost(id.toString());
            Thread.sleep(30);
        }
    }

    @ParameterizedTest
    @MethodSource("getSavedPostsOfUserTestData")
    void getSavedPostsOfUser(String userName, Integer pageNumber, Integer pageSize, Integer day, Integer month,
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
        LocalDate currentDate = LocalDate.now();

        // Get the day, month, and year
        int day = currentDate.getDayOfMonth();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();

        return Stream.of(
                Arguments.of("PostServiceUsername1", 0, 10, null, null, null, null, List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("PostServiceUsername2", 0, 10, null, null, null, null, List.of("2 user1", "1 user1", "0 user1")),
                Arguments.of("PostServiceUsername1", 0, 10, day, month, year, null, List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("PostServiceUsername2", 0, 10, day, month, year, null, List.of("2 user1", "1 user1", "0 user1")),
                Arguments.of("PostServiceUsername1", 0, 10, (day + 5) % 30, month, year, null, List.of()),
                Arguments.of("PostServiceUsername2", 0, 10, (day + 5) % 30, month, year, null, List.of()),
                Arguments.of("PostServiceUsername1", 0, 10, null, null, null, List.of(LOVE), List.of("2 user2", "0 user2")),
                Arguments.of("PostServiceUsername2", 0, 10, day, month, year, List.of(LOVE, HAPPY), List.of("1 user1", "0 user1")),
                Arguments.of("PostServiceUsername1", 0, 10, day, month, year, List.of(LOVE, HAPPY, SAD),List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("PostServiceUsername1", 0, 10, null, null, null, List.of(LOVE, HAPPY, SAD, INSPIRE),List.of("2 user2", "1 user2", "0 user2")),
                Arguments.of("PostServiceUsername2", 0, 10, null, null, null, List.of(SAD),List.of("2 user1")),
                Arguments.of("PostServiceUsername2", 0, 10, null, null, null, List.of(INSPIRE),List.of()),// return noting
                Arguments.of("PostServiceUsername2", 0, 10, null, null, null, List.of(), List.of("2 user1", "1 user1", "0 user1"))// return all
        );
    }
}
