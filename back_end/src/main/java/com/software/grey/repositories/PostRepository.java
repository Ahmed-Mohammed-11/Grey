package com.software.grey.repositories;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
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
    Page<Post> findDiaryByUsernameAndDayMonthYear(
            @Param("userName") String userName,
            @Param("day") Integer day,
            @Param("month") Integer month,
            @Param("year") Integer year,
            Pageable pageable
    );

    @Query(value = """
    SELECT DISTINCT p.id, p.text, p.post_time, u.id as user_id
    FROM post p
    JOIN user u ON u.id = p.user_id
    JOIN post_feelings pf ON pf.post_id = p.id
    WHERE u.username != :userName
      AND (:feelings is null OR pf.feeling IN :feelings)
    ORDER BY p.post_time DESC
    """,
            countQuery = """
    SELECT count(p.id) FROM post p JOIN user u ON u.id = p.user_id
    JOIN post_feelings pf ON pf.post_id = p.id
    WHERE u.username != :userName AND (:feelings is () OR pf.feeling IN :feelings)
    """, nativeQuery = true)
    Page<Post> findFeed(@Param("userName") String userName, @Param("feelings") List<String> feelings, Pageable pageable);
}
