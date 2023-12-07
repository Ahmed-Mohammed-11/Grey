package com.software.grey.recommendationsystem;

import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CombinerImp implements Combiner{

    @Override
    public List<Post> combine(StrategyPercentage strategyPercentage,  List<List<Post>> posts) {
        return null;
    }
}
