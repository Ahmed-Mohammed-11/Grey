package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import com.software.grey.repositories.PostRepository;
import com.software.grey.services.implementations.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Component
public class SameFeelingStrat extends RecommendationStrategy{
    private PostService postService;
    private PostRepository postRepository;

    @Override
    public List<Post> recommend(User user, Integer pageNumber, Integer count) {
        // get most common user's feelings
        List<FeelingCountProjection> feelingsCount = postService.getCountOfPostedFeelings(user);
        // convert to <feeling, percentage> key-pair map
        Map<Feeling, Double> percentageMap = getFeelingPercentage(feelingsCount);

        // retrieve posts from database
        List<Post> postsRecommended = new ArrayList<>();
        for (Map.Entry<Feeling, Double> entry : percentageMap.entrySet()) {
            int numOfPosts = (int)(entry.getValue() * count);
            Pageable page = PageRequest.of(pageNumber, numOfPosts);
            postsRecommended.addAll(postRepository.findByPostFeelingsAndUserIdNot(entry.getKey(), user.getId() ,page));
        }

        return postsRecommended;
    }
}
