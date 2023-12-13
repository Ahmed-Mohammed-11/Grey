package com.software.grey.recommendationsystem;

import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Recommender {
    private Combiner combiner;
    private SameFeelingStrat sameFeelingStrat;
    private InverseFeelingStrat inverseFeelingStrat;
    private CollaborativeFeelingStrat collaborativeFeelingStrat;
    private SecurityUtils securityUtils;

    @Value("${grey.same.feeling}")
    private int sameFeelingPercentage;
    @Value("${grey.inverse.feeling}")
    private int inverseFeelingPercentage;
    @Value("${grey.collaborative.feeling}")
    private int collaborativeFeelingPercentage;

    public Recommender(Combiner combiner,
                       SameFeelingStrat sameFeelingStrat,
                       InverseFeelingStrat inverseFeelingStrat,
                       CollaborativeFeelingStrat collaborativeFeelingStrat,
                       SecurityUtils securityUtils) {
        this.combiner = combiner;
        this.sameFeelingStrat = sameFeelingStrat;
        this.inverseFeelingStrat = inverseFeelingStrat;
        this.collaborativeFeelingStrat = collaborativeFeelingStrat;
        this.securityUtils = securityUtils;
    }

    public List<Post> recommend(PostFilterDTO postFilterDTO) {
        StrategyPercentage stratPercent = buildStrategies();

        // request posts here and pass it to combiner
        List<Post> sameFeelingPosts = getSameFeelingRecommendation(postFilterDTO, stratPercent);
        List<Post> inverseFeelingPosts = getInverseFeelingRecommendation(postFilterDTO, stratPercent);
        List<Post> collaborativeFeelingPosts = getCollaborativeFeelingRecommendation(postFilterDTO, stratPercent);

        List<List<Post>> aggreagtedPosts = new ArrayList<>();
        aggreagtedPosts.add(sameFeelingPosts);
        aggreagtedPosts.add(inverseFeelingPosts);
        aggreagtedPosts.add(collaborativeFeelingPosts);

        // sort by date, and remove duplicates
        return combiner.combine(aggreagtedPosts);
    }

    private StrategyPercentage buildStrategies() {
        StrategyPercentage strategyPercentage = new StrategyPercentage();
        strategyPercentage.addNewStrategyPercentage(sameFeelingStrat, sameFeelingPercentage);
        strategyPercentage.addNewStrategyPercentage(inverseFeelingStrat, inverseFeelingPercentage);
        strategyPercentage.addNewStrategyPercentage(collaborativeFeelingStrat, collaborativeFeelingPercentage);
        return strategyPercentage;
    }

    private List<Post> getSameFeelingRecommendation(PostFilterDTO postFilterDTO, StrategyPercentage stratPercent) {
        return sameFeelingStrat.recommend(
                securityUtils.getCurrentUser(),
                postFilterDTO.getPageNumber(),
                calculateNumberOfPostsFromPercentage(
                        postFilterDTO.getPageSize(),
                        stratPercent.getStrategyPercentage(sameFeelingStrat) / 100.0
                )
        );
    }

    private List<Post> getInverseFeelingRecommendation(PostFilterDTO postFilterDTO, StrategyPercentage stratPercent) {
        return inverseFeelingStrat.recommend(
                securityUtils.getCurrentUser(),
                postFilterDTO.getPageNumber(),
                calculateNumberOfPostsFromPercentage(
                        postFilterDTO.getPageSize(),
                        stratPercent.getStrategyPercentage(inverseFeelingStrat) / 100.0
                )
        );
    }

    private List<Post> getCollaborativeFeelingRecommendation(PostFilterDTO postFilterDTO, StrategyPercentage stratPercent) {
        return collaborativeFeelingStrat.recommend(
                securityUtils.getCurrentUser(),
                postFilterDTO.getPageNumber(),
                calculateNumberOfPostsFromPercentage(
                        postFilterDTO.getPageSize(),
                        stratPercent.getStrategyPercentage(collaborativeFeelingStrat) / 100.0
                )
        );
    }

    private int calculateNumberOfPostsFromPercentage(int totalSize, double percentage) {
        return (int)(percentage * totalSize);
    }
}
