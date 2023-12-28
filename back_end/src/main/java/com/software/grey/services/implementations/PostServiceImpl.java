package com.software.grey.services.implementations;

import com.software.grey.exceptions.exceptions.PostNotFoundException;
import com.software.grey.exceptions.exceptions.UserNotAuthorizedException;
import com.software.grey.exceptions.exceptions.UserReportedPostBeforeException;
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
import com.software.grey.services.PostService;
import com.software.grey.utils.ErrorMessages;
import com.software.grey.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ReportedPostRepository reportedPostRepository;
    private PostMapper postMapper;
    private SecurityUtils securityUtils;

    public String add(PostDTO postDTO) {
        User user = securityUtils.getCurrentUser();
        Post post = postMapper.toPost(postDTO);
        post.setUser(user);
        post.setPostTime(Timestamp.from(Instant.now()));
        postRepository.save(post);
        return post.getId();
    }

    public void report(String postId) {
        Post post = findPostById(postId);
        User reporter = securityUtils.getCurrentUser();

        ReportedPost reportedPost = ReportedPost.builder()
                .post(post)
                .reporter(reporter)
                .build();

        if (userReportedPostBefore(post, reporter))
            throw new UserReportedPostBeforeException(ErrorMessages.POST_REPORTED_BEFORE);

        reportedPostRepository.save(reportedPost);
    }

    private boolean userReportedPostBefore(Post post, User reporter) {
        return reportedPostRepository.existsById(new ReportedPostId(post, reporter));
    }

    public Post findPostById(String id) {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(ErrorMessages.POST_NOT_FOUND));
    }

    public Page<PostDTO> getReportedPosts(PostFilterDTO postFilterDTO) {
        Pageable pageable = PageRequest.of(
                postFilterDTO.getPageNumber(),
                postFilterDTO.getPageSize());

        return reportedPostRepository.findByOrderByPostPostTimeDesc(pageable)
                .map(ReportedPost::getPost)
                .map(postMapper::toPostDTO);
    }

    public Page<PostDTO> getDiary(PostFilterDTO postFilterDTO) {
        String userName = securityUtils.getCurrentUserName();
        Pageable pageable = PageRequest.of(
                postFilterDTO.getPageNumber(),
                postFilterDTO.getPageSize());
        return postRepository.findDiaryByUsernameAndDayMonthYearSortedByDate(
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
                .map(list -> list.stream().map(Enum::name).toList())
                .orElseGet(() -> Arrays.stream(Feeling.values())
                        .map(Enum::name)
                        .toList());

        Pageable pageable = PageRequest.of(
                postFilterDTO.getPageNumber(),
                postFilterDTO.getPageSize());

        return postRepository.findFeed(userName, feelings, pageable)
                .map(postMapper::toPostDTO);
    }

    public void delete(String postId) {
        if (postId == null) {
            throw new NullPointerException(ErrorMessages.POST_ID_NULL);
        }
        String currentUserId = securityUtils.getCurrentUserId();
        Post post = findPostById(postId);
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new UserNotAuthorizedException(ErrorMessages.USER_NOT_AUTHORIZED);
        }
        postRepository.deleteById(postId);
    }

    public List<FeelingCountProjection> getCountOfPostedFeelings(User user) {
        return postRepository.findCountOfFeelingsByUser(user.getId());
    }

    public List<Post> getByFeelings(Feeling feeling, String userId, Pageable page) {
        return postRepository.findByPostFeelingsAndUserIdNot(feeling, userId, page);
    }

    public void deleteReportedPost(String postId) {
        removeReportedPost(postId);
        Post post = findPostById(postId);
        postRepository.delete(post);
    }

    public void removeReportedPost(String postId) {
        if (!reportedPostRepository.existsByPostId(postId)) {
            throw new PostNotFoundException(ErrorMessages.POST_ALREADY_DELETED);
        }
        reportedPostRepository.deleteByPostId(postId);
    }
}
