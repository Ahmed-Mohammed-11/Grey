package com.software.grey.services;

import com.software.grey.SavedPostEnum;

public interface SavedPostService {
    SavedPostEnum toggleSavedPost(String postId);
}
