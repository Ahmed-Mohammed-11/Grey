package com.software.grey.services.implementations;

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
    public SavedPostEnum toggleSavedPost(String postId) {
        try {
            return toggle(UUID.fromString(postId));
        } catch (IllegalArgumentException e) {
            return SavedPostEnum.NOT_FOUND;
        }
    }


    private SavedPostEnum toggle(UUID postId) {
        User user = securityUtils.getCurrentUser();
        if (postId == null || user == null) {
            return SavedPostEnum.NOT_FOUND;
        }
        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent() && userIsNotThePostAuthor(user, post.get())) {
            // create saved post ID
            SavedPostId savedPostId = new SavedPostId(user, post.get());

            if (savedPostRepository.existsById(savedPostId)) {
                savedPostRepository.deleteById(savedPostId);
                return SavedPostEnum.REMOVED;
            } else {
                savedPostRepository.save(new SavedPost(user, post.get()));
                return SavedPostEnum.SAVED;
            }
        }
        return SavedPostEnum.NOT_FOUND;
    }

    private boolean userIsNotThePostAuthor(User user, Post post) {
        return !user.getId().equals(post.getUser().getId());
    }

    public Page<PostDTO> getSavedPosts(PostFilterDTO postFilterDTO) {
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

        Page<Post> pos = savedPostRepository.findSavedPostsByUsernameAndDayMonthYear(feelings,
                postFilterDTO.getDay(),
                postFilterDTO.getMonth(),
                postFilterDTO.getYear(), pageable).map(SavedPost::getPost);
        return pos
                .map(postMapper::toPostDTO);
    }
}
