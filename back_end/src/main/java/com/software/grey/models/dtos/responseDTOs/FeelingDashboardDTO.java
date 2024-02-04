package com.software.grey.models.dtos.responseDTOs;

import com.software.grey.models.projections.FeelingCountProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeelingDashboardDTO {
    public long totalNumberOfPosts;
    public long totalNumberOfUserPosts;
    public List<FeelingCountProjection> userFeelings;
    public List<FeelingCountProjection> globalFeelings;
}
