package com.software.grey.services.impl;

import com.software.grey.mapper.impl.SavedPostMapper;
import com.software.grey.models.dtos.SavedPostDto;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.SavedPostId;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.PostRepository;
import com.software.grey.repositories.SavedPostRepository;
import com.software.grey.repositories.UserRepository;
import com.software.grey.services.SavedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SavedPostServiceImpl implements SavedPostService {

    SavedPostMapper savedPostMapper;
    SavedPostRepository savedPostRepository;
    UserRepository userRepository;
    PostRepository postRepository;


    @Autowired
    SavedPostServiceImpl (SavedPostMapper savedPostMapper, SavedPostRepository savedPostRepository,
                          UserRepository userRepository, PostRepository postRepository) {
        this.savedPostRepository = savedPostRepository;
        this.savedPostMapper = savedPostMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    @Override
    public Integer saveUnsavePost(SavedPostDto savedPostDto) {
        SavedPost savedPost = savedPostMapper.mapFrom(savedPostDto);
        User user = savedPost.getUser();
        Post post = savedPost.getPost();
        if (userRepository.existsById(user.getId()) && postRepository.existsById(post.getId())) {
            SavedPostId savedPostId = new SavedPostId(savedPostDto.getUser(), savedPostDto.getPost());
            if (savedPostRepository.existsById(savedPostId)) {
                savedPostRepository.deleteById(savedPostId);
                return 0;
            } else {
                savedPostRepository.save(savedPost);
                return 1;
            }
        }
        return -1;
    }
}
