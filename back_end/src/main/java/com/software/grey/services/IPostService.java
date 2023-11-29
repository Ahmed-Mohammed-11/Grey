package com.software.grey.services;

import com.software.grey.models.dtos.PostDTO;

import java.util.UUID;

public interface IPostService {
    UUID add(PostDTO postDTO);
}
