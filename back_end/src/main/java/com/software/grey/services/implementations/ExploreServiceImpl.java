package com.software.grey.services.implementations;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.dtos.RecommendedPostsDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.mappers.PostMapper;
import com.software.grey.recommendationsystem.Recommender;
import com.software.grey.services.ExploreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExploreServiceImpl implements ExploreService {

    private Recommender recommender;

    private PostMapper postMapper;

    public RecommendedPostsDTO getRecommendedPosts(int pageNumber, int pageSize) {

        PostFilterDTO postFilterDTO = PostFilterDTO.builder().pageNumber(pageNumber).pageSize(pageSize).build();
        List<Post> recommendedPosts = recommender.recommend(postFilterDTO);
        List<PostDTO> postsDTO = recommendedPosts.stream().map(postMapper::toPostDTO).toList();

        // convert postsDTO to RecommendedPostsDTO
        return new RecommendedPostsDTO(postsDTO, recommendedPosts.isEmpty());
    }

}
