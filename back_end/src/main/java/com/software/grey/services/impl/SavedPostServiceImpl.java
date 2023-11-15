package com.software.grey.services.impl;

import com.software.grey.SavedPostEnum;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.SavedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.SavedPostRepository;
import com.software.grey.repositories.UserRepository;
import com.software.grey.services.SavedPostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class SavedPostServiceImpl implements SavedPostService {

    private SavedPostRepository savedPostRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Override
    public SavedPostEnum saveUnsavePost(UUID postId) {
        User user = getUser();
        if (postId == null || user == null) {
            return SavedPostEnum.NOT_FOUND;
        }
        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {
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

    User getUser() {
        Iterable<User> users = userRepository.findAll();
        if (users.iterator().hasNext()) {
            return users.iterator().next();
        }
        return null;
    }
}
