package com.software.grey.services.implementations;

import com.software.grey.exceptions.exceptions.DataNotFoundException;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.mappers.PostMapper;
import com.software.grey.repositories.PostRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService implements IPostService {

    private PostRepository postRepository;

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

    public Post findPostById(UUID id){
        return postRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found"));
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
                .orElse(Collections.emptyList())
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(
                postFilterDTO.getPageNumber(),
                postFilterDTO.getPageSize());

        return postRepository.findFeed(userName, feelings, pageable)
                .map(postMapper::toPostDTO);
    }
}
