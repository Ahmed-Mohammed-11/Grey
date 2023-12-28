package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import com.software.grey.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Component
public class SameFeelingStrat extends RecommendationStrategy {

    private PostService postService;

    @Override
    public List<Post> recommend(User user, Integer pageNumber, Integer count) {
        // get most common user's feelings
        List<FeelingCountProjection> feelingsCount = postService.getCountOfPostedFeelings(user);
        // convert to <feeling, percentage> key-pair map
        Map<Feeling, Double> percentageMap = getFeelingPercentage(feelingsCount);

        // retrieve posts from database
        return percentageMap.entrySet().stream()
                .flatMap(entry -> {
                    int numOfPosts = (int) Math.ceil(entry.getValue() * count);
                    Pageable page = PageRequest.of(pageNumber, numOfPosts);
                    return postService.getByFeelings(entry.getKey(), user.getId(), page).stream();
                })
                .toList();
    }
}
