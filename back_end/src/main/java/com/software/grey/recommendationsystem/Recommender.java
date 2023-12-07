package com.software.grey.recommendationsystem;

import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class Recommender {
    private Combiner combiner;
    private SameFeelingStrat sameFeelingStrat;
    private SecurityUtils securityUtils;

    public List<Post> recommend(PostFilterDTO postFilterDTO) {
        StrategyPercentage stratPercent = buildStrategies();

        // request posts here and pass it to combiner
        List<Post> SameFeelingPosts = getSameFeelingRecommendation(postFilterDTO, stratPercent);

        List<List<Post>> aggregatedPosts = new ArrayList<>();
        aggregatedPosts.add(SameFeelingPosts);

        // sort by date, and remove duplicates
        return combiner.combine(stratPercent, aggregatedPosts);
    }

    private StrategyPercentage buildStrategies() {
        StrategyPercentage strategyPercentage = new StrategyPercentage();
        strategyPercentage.addNewStrategyPercentage(sameFeelingStrat, 100);
        return strategyPercentage;
    }

    private List<Post> getSameFeelingRecommendation(PostFilterDTO postFilterDTO, StrategyPercentage stratPercent) {
        return sameFeelingStrat.recommend(
                securityUtils.getCurrentUser(),
                postFilterDTO.getPageNumber(),
                calculateNumberOfPostsFromPercentage(
                        postFilterDTO.getPageSize(),
                        stratPercent.getStrategyPercentage(sameFeelingStrat)
                )
        );
    }


    private int calculateNumberOfPostsFromPercentage(int totalSize, double percentage) {
        return (int)(percentage * totalSize);
    }
}
