package com.software.grey.services;

import com.software.grey.SavedPostEnum;

import java.util.UUID;

public interface SavedPostService {
    SavedPostEnum saveUnsavePost(UUID postId);
}
