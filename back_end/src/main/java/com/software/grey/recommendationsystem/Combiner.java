package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;

import java.util.List;

public interface Combiner {

    List<Post> combine(List<List<Post>> posts);
}
