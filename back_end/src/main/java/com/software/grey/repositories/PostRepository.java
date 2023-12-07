package com.software.grey.repositories;

import com.software.grey.models.entities.Post;

import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {


    Post findByUser(User user);

    @Query("""
        SELECT p FROM Post p
        WHERE p.user.username = :userName
        AND (:day IS NULL OR DAY(p.postTime) = :day)
        AND (:month IS NULL OR MONTH(p.postTime) = :month)
        AND (:year IS NULL OR YEAR(p.postTime) = :year)
        """)
    Page<Post> findAllByUsernameAndDayMonthYear(
            @Param("userName") String userName,
            @Param("day") Integer day,
            @Param("month") Integer month,
            @Param("year") Integer year,
            Pageable pageable
    );

    @Query(value = """ 
            SELECT feeling, COUNT(feeling) AS feelingCount
               FROM (
                   SELECT pf.feeling
                   FROM Post p
                   JOIN post_feelings pf ON pf.post_id = p.id
                   WHERE p.user_id = ?1
                   ORDER BY p.post_time DESC
                   LIMIT 5
               ) AS latest_posts
            GROUP BY latest_posts.feeling
            ORDER BY feelingCount DESC;
            """, nativeQuery = true)
    public List<FeelingCountProjection> findCountOfFeelingsByUser(String id);

    // TODO make sure that user's own post is not returned
    public List<Post> findByPostFeelings(Feeling feeling, Pageable pageable);
}
