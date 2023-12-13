package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class RecommendationStrategy {
    public abstract List<Post> recommend(User user, Integer pageNumber, Integer count);
    protected Map<Feeling, Double> getFeelingPercentage(List<FeelingCountProjection> feelingsCount) {
        Integer totalFeelingSize = getSumOfFeelings(feelingsCount);
        Map<Feeling, Double> percentageMap = new LinkedHashMap<>();

        for (FeelingCountProjection f : feelingsCount) {
            if(f.getFeelingCount() == 0 || totalFeelingSize == 0)
                continue;
            Double percentage = f.getFeelingCount() / Double.valueOf(totalFeelingSize);
            percentageMap.put(f.getFeeling(), percentage);
        }
        return percentageMap;
    }

    protected Integer getSumOfFeelings(List<FeelingCountProjection> feelingsCount) {
        return feelingsCount.stream().mapToInt(FeelingCountProjection::getFeelingCount).sum();
    }
}