package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.projections.FeelingCountProjection;
import com.software.grey.repositories.PostRepository;
import com.software.grey.services.implementations.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class CollaborativeFeelingStrat extends RecommendationStrategy {
    private PostService postService;
    private PostRepository postRepository;

    @Override
    public List<Post> recommend(User user, Integer pageNumber, Integer count) {
        // get most common user's feelings
        List<FeelingCountProjection> feelingsCount = postService.getCountOfPostedFeelings(user);
        if(feelingsCount.isEmpty() || feelingsCount.get(0).getFeelingCount() == 0)
            return new ArrayList<>();
        Pageable page = PageRequest.of(pageNumber, count);
        return postRepository.findByCollaborativeFiltering(feelingsCount.get(0).getFeeling().toString(), user.getId(), page);
    }

}
