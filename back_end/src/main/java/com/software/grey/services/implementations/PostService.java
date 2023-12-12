package com.software.grey.services.implementations;

import com.software.grey.exceptions.exceptions.DataNotFoundException;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.mappers.PostMapper;
import com.software.grey.models.projections.FeelingCountProjection;
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
import java.util.List;
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

    public Page<PostDTO> getAll(PostFilterDTO postFilterDTO) {
        String userName = securityUtils.getCurrentUserName();
        Pageable pageable = PageRequest.of(postFilterDTO.getPageNumber(), postFilterDTO.getPageSize(), Sort.by("postTime").descending());
        return postRepository.findAllByUsernameAndDayMonthYear(
                userName,
                postFilterDTO.getDay(),
                postFilterDTO.getMonth(),
                postFilterDTO.getYear(),
                pageable).map(postMapper::toPostDTO);
    }

    public List<FeelingCountProjection> getCountOfPostedFeelings(User user) {
        return postRepository.findCountOfFeelingsByUser(user.getId());
    }
}
