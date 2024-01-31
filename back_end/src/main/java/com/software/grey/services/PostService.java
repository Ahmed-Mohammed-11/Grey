package com.software.grey.services;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    String add(PostDTO postDTO);

    void report(String postId);

    Post findPostById(String id);

    Page<PostDTO> getReportedPosts(PostFilterDTO postFilterDTO);

    Page<PostDTO> getDiary(PostFilterDTO postFilterDTO);

    Page<PostDTO> getFeed(PostFilterDTO postFilterDTO);

    void delete(String postId);

    List<FeelingCountProjection> getCountOfPostedFeelings(User user);

    List<Post> getByFeelings(Feeling feeling, String userId, Pageable page);

    void deleteReportedPost(String postId);

    void removeReportedPost(String postId);
}
