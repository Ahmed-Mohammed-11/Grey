package com.software.grey.services.implementations;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.mappers.PostMapper;

import com.software.grey.recommendationsystem.Recommender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExploreService {

    private Recommender recommender;
    private PostMapper postMapper;

    public List<PostDTO> getRecommendedPosts(int pageNumber, int pageSize) {
        PostFilterDTO postFilterDTO = PostFilterDTO.builder().pageNumber(pageNumber).pageSize(pageSize).build();
        List<Post> recPosts = recommender.recommend(postFilterDTO);
        List<PostDTO> postsDTO = new ArrayList<>();

        for (Post p : recPosts)
            postsDTO.add(postMapper.toPostDTO(p));

        return postsDTO;
    }
}
