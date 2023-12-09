package com.software.grey.repositories;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.SavedPostId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, SavedPostId> {

    //parametrized query to get all saved posts of specific user by his id
    @Query("""
        SELECT post FROM Post post
        JOIN SavedPost savedPost ON post.id = savedPost.post.id
        WHERE savedPost.user.id = :userId
        AND (:day IS NULL OR DAY(post.postTime) = :day)
        AND (:month IS NULL OR MONTH(post.postTime) = :month)
        AND (:year IS NULL OR YEAR(post.postTime) = :year)
        """)
    Page<Post> findPostsByUserAndFeelingsAndDate(
            @Param("userId") String userId,
            @Param("day") Integer day,
            @Param("month") Integer month,
            @Param("year") Integer year,
            Pageable pageable
    );
}
