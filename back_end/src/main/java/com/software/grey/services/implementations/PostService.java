package com.software.grey.services.implementations;

import com.software.grey.exceptions.exceptions.DataNotFoundException;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.mappers.PostMapper;
import com.software.grey.repositories.PostRepository;
import com.software.grey.services.IPostService;
import com.software.grey.services.UserService;
import com.software.grey.utils.SecurityUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PostService implements IPostService {

    private PostRepository postRepository;

    private PostMapper postMapper;

    private UserService userService;

    private SecurityUtils securityUtils;

    public UUID add(PostDTO postDTO) {
        Post post = postMapper.toPost(postDTO);
        post.setUser(userService.findByUserName (securityUtils.getCurrentUserName()));
        postRepository.save(post);
        return post.getId();
    }

    public Post findPostById(UUID id){
        return postRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found"));
    }
}
