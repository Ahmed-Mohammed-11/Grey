package com.software.grey.services.implementations;

import com.software.grey.exceptions.exceptions.UserReportedPostBeforeException;
import com.software.grey.exceptions.exceptions.DataNotFoundException;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.ReportedPost;
import com.software.grey.models.entities.ReportedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.mappers.PostMapper;
import com.software.grey.models.projections.FeelingCountProjection;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.ReportedPostRepository;
import com.software.grey.services.IPostService;
import com.software.grey.services.UserService;
import com.software.grey.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.UUID;

import static com.software.grey.utils.ErrorMessages.POST_REPORTED_BEFORE;

@Service
@AllArgsConstructor
public class PostService implements IPostService {

    private PostRepository postRepository;

    private ReportedPostRepository reportedPostRepository;

    private PostMapper postMapper;

    private UserService userService;

    private SecurityUtils securityUtils;

    public UUID add(PostDTO postDTO) {
        Post post = postMapper.toPost(postDTO);
        String userName = securityUtils.getCurrentUserName();
        User user = userService.findByUserName(userName);
        post.setUser(user);
        post.setPostTime(Timestamp.from(Instant.now()));
        postRepository.save(post);
        return post.getId();
    }

    public void report(String postId) {
        UUID postUUID;
        try {
            postUUID = UUID.fromString(postId);
        } catch (IllegalArgumentException e) {
            throw new DataNotFoundException("Invalid post id");
        }

        Post post = findPostById(postUUID);
        User reporter = securityUtils.getCurrentUser();

        ReportedPost reportedPost = ReportedPost.builder()
                .post(post)
                .reporter(reporter)
                .build();

        if(userReportedPostBefore(post, reporter))
            throw new UserReportedPostBeforeException(POST_REPORTED_BEFORE);

        reportedPostRepository.save(reportedPost);
    }

    private boolean userReportedPostBefore(Post post, User reporter) {
        return reportedPostRepository.existsById(new ReportedPostId(post, reporter));
    }

    public Post findPostById(UUID id){
        return postRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Post not found"));
    }

    public Page<PostDTO> getDiary(PostFilterDTO postFilterDTO) {
        String userName = securityUtils.getCurrentUserName();
        Pageable pageable = PageRequest.of(
                postFilterDTO.getPageNumber(),
                postFilterDTO.getPageSize(),
                Sort.by("postTime").descending());
        return postRepository.findDiaryByUsernameAndDayMonthYear(
                userName,
                postFilterDTO.getDay(),
                postFilterDTO.getMonth(),
                postFilterDTO.getYear(),
                pageable).map(postMapper::toPostDTO);
    }

    public Page<PostDTO> getFeed(PostFilterDTO postFilterDTO) {
        String userName = securityUtils.getCurrentUserName();
        List<String> feelings = Optional.ofNullable(postFilterDTO.getFeelings())
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(Enum::name).collect(Collectors.toList()))
                .orElseGet(() -> Arrays.stream(Feeling.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()));

        Pageable pageable = PageRequest.of(
                postFilterDTO.getPageNumber(),
                postFilterDTO.getPageSize());

        return postRepository.findFeed(userName, feelings, pageable)
                .map(postMapper::toPostDTO);
    }

    public void delete(String postId) {

        String currentUserId = securityUtils.getCurrentUserId();
        UUID uuid;
        try {
            uuid = UUID.fromString(postId);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid post id");
        }

        Post post = findPostById(UUID.fromString(postId));
        if(!post.getUser().getId().toString().equals(currentUserId)){
            throw new DataNotFoundException("You are not authorized to delete this post");
        }

        postRepository.deleteById(post.getId());
    }

    public List<FeelingCountProjection> getCountOfPostedFeelings(User user) {
        return postRepository.findCountOfFeelingsByUser(user.getId());
    }

    public List<Post> getByFeelings(Feeling feeling, String userId, Pageable page){
        return postRepository.findByPostFeelingsAndUserIdNot(feeling, userId, page);
    }
}
