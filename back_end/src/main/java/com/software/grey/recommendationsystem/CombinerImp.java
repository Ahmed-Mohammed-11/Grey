package com.software.grey.recommendationsystem;

import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
public class CombinerImp implements Combiner{

    @Override
    public List<Post> combine(List<List<Post>> posts) {
        Set<Post> set = new TreeSet<>();
        // merge sorted lists into one
        for(List<Post> postList: posts)
            for (Post post: postList)
                set.add(post);

        return new ArrayList<>(set);
    }
}
