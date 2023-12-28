package com.software.grey.services;

import com.software.grey.models.dtos.RecommendedPostsDTO;

public interface ExploreService {

    RecommendedPostsDTO getRecommendedPosts(int pageNumber, int pageSize);
}
