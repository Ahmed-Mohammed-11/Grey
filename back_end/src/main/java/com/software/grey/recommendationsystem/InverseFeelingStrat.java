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

import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Component
public class InverseFeelingStrat extends RecommendationStrategy{
    private PostService postService;

    @Override
    public List<Post> recommend(User user, Integer pageNumber, Integer count) {
        // get most common user's feelings
        List<FeelingCountProjection> feelingsCount = postService.getCountOfPostedFeelings(user);
        // convert to <feeling, percentage> key-pair map
        Map<Feeling, Double> percentageMap = getFeelingPercentage(feelingsCount);

        if (percentageMap.get(Feeling.SAD) == null)
            return Collections.emptyList();

        Pageable page = PageRequest.of(pageNumber, count);
        return postService.getByFeelings(Feeling.HAPPY, user.getId(), page);
    }
}
