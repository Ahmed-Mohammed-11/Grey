package com.software.grey.services;

import com.software.grey.SavedPostEnum;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.SavedPostId;
import org.springframework.data.domain.Page;

public interface SavedPostService {
    SavedPostEnum toggleSavedPost(String postId);
    Page<PostDTO> getSavedPosts(PostFilterDTO postFilterDTO);
}
