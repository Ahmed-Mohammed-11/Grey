package com.software.grey.recommendationsystem;

import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;

import java.util.List;

public interface Combiner {
    public List<Post> combine(List<List<Post>> posts);
}
