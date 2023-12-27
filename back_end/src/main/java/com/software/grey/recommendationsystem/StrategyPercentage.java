package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyPercentage {

    private final Map<RecommendationStrategy, Integer> percentageMap;

    private final Map<RecommendationStrategy, List<Post>> postsMap;

    public StrategyPercentage() {
        this.percentageMap = new HashMap<>();
        this.postsMap = new HashMap<>();
    }

    public void addNewStrategyPercentage(RecommendationStrategy recommendationStrategy, Integer precentage) {
        this.percentageMap.put(recommendationStrategy, precentage);
    }

    public void addNewStrategyPosts(RecommendationStrategy recommendationStrategy, List<Post> posts) {
        this.postsMap.put(recommendationStrategy, posts);
    }

    public void removeStrategy(RecommendationStrategy recommendationStrategy) {
        this.percentageMap.remove(recommendationStrategy);
        this.postsMap.remove(recommendationStrategy);
    }

    public Integer getStrategyPercentage(RecommendationStrategy recommendationStrategy) {
        return this.percentageMap.get(recommendationStrategy);
    }

    public List<Post> getStrategyPosts(RecommendationStrategy recommendationStrategy) {
        return this.postsMap.get(recommendationStrategy);
    }
}
