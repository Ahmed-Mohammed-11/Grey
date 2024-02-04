package com.software.grey.services.implementations;


import com.software.grey.models.dtos.responseDTOs.FeelingDashboardDTO;
import com.software.grey.models.projections.FeelingCountProjection;
import com.software.grey.repositories.PostRepository;
import com.software.grey.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FeelingDashboardService {

    private PostRepository postRepository;
    private SecurityUtils securityUtils;

    public FeelingDashboardDTO getFeelingDashboardContent() {
        String user_id = securityUtils.getCurrentUserId();
        List<FeelingCountProjection> feelingCountProjectionOfUserFeelings = postRepository.findFeelingsFrequencyForSpecificUser(user_id);
        List<FeelingCountProjection> feelingCountProjectionOfGlobalFeelings = postRepository.findGlobalFeelingFrequency();
        long totalNumberOfPosts = postRepository.countPostsToday();
        long totalNumberOfUserPosts = postRepository.countPostsTodayByUserId(user_id);
        return FeelingDashboardDTO.builder()
                .totalNumberOfPosts(totalNumberOfPosts)
                .totalNumberOfUserPosts(totalNumberOfUserPosts)
                .userFeelings(feelingCountProjectionOfUserFeelings)
                .globalFeelings(feelingCountProjectionOfGlobalFeelings)
                .build();
    }
}
