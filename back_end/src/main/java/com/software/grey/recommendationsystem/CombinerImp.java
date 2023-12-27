package com.software.grey.recommendationsystem;

import com.software.grey.models.entities.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
public class CombinerImp implements Combiner{

    /**
     * @param posts A list of recommended posts, retrieved by any recommendation heuristic
     * @return A list of posts, sorted by decreasing order of post time
     */
    @Override
    public List<Post> combine(List<List<Post>> posts) {
        Set<Post> set = new TreeSet<>();
        // merge sorted lists into one using a set
        for(List<Post> postList: posts)
            for (Post post: postList)
                set.add(post);

        return new ArrayList<>(set);
    }
}
