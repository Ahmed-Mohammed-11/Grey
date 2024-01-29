package com.software.grey.services.implementations;

import com.software.grey.exceptions.exceptions.UserIsAuthorException;
import com.software.grey.SavedPostEnum;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.SavedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.mappers.PostMapper;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.SavedPostRepository;
import com.software.grey.services.SavedPostService;
import com.software.grey.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SavedPostServiceImpl implements SavedPostService {

    private SavedPostRepository savedPostRepository;
    private PostRepository postRepository;
    private SecurityUtils securityUtils;
    private PostMapper postMapper;

    @Override
    public String toggleSavedPost(String postId) {
        User user = securityUtils.getCurrentUser();
        if (postId == null || user == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent() && userIsNotThePostAuthor(user, post.get())) {
            // create saved post ID
            SavedPostId savedPostId = new SavedPostId(user, post.get());

            if (savedPostRepository.existsById(savedPostId)) {
                savedPostRepository.deleteById(savedPostId);
                return "Removed successfully";
            } else {
                savedPostRepository.save(new SavedPost(user, post.get(), Timestamp.from(Instant.now())));
                return "Saved successfully";
            }
        }
        throw new UserIsAuthorException("You have written this post");
    }

    private boolean userIsNotThePostAuthor(User user, Post post) {
        return !user.getId().equals(post.getUser().getId());
    }

    public Page<PostDTO> getSavedPosts(PostFilterDTO postFilterDTO) {
        String userName = securityUtils.getCurrentUserName();
        List<String> feelings = Optional.ofNullable(postFilterDTO.getFeelings())
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(Enum::name).collect(Collectors.toList()))
                .orElseGet(() -> Arrays.stream(Feeling.values()).map(Enum::name).collect(Collectors.toList()));

        Pageable pageable = PageRequest.of(
                postFilterDTO.getPageNumber(),
                postFilterDTO.getPageSize());

        return savedPostRepository.findSavedPostsByUsernameAndDayMonthYear(userName, feelings,
                postFilterDTO.getDay(),
                postFilterDTO.getMonth(),
                postFilterDTO.getYear(), pageable).map(SavedPost::getPost)
                .map(postMapper::toPostDTO);
    }
}
