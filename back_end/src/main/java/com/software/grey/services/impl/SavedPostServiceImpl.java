package com.software.grey.services.impl;

import com.software.grey.exceptions.exceptions.UserIsAuthorException;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.SavedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.SavedPostRepository;
import com.software.grey.services.SavedPostService;
import com.software.grey.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SavedPostServiceImpl implements SavedPostService {

    private SavedPostRepository savedPostRepository;
    private PostRepository postRepository;
    private SecurityUtils securityUtils;

    @Override
    public String toggleSavedPost(String postId) {
        try {
            return toggle(UUID.fromString(postId));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid post ID");
        }
    }


    private String toggle(UUID postId) {
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
                savedPostRepository.save(new SavedPost(user, post.get()));
                return "Saved successfully";
            }
        }
        throw new UserIsAuthorException("You have written this post");
    }

    private boolean userIsNotThePostAuthor(User user, Post post) {
        return !user.getId().equals(post.getUser().getId());
    }
}
