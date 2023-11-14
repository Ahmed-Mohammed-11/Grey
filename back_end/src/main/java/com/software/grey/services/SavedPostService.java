package com.software.grey.services;

import com.software.grey.models.dtos.SavedPostDto;

public interface SavedPostService {
    Integer saveUnsavePost(SavedPostDto savedPostDto);
}
