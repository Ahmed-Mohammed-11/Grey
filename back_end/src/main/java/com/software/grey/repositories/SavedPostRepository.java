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

import java.util.List;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, SavedPostId> {
    @Query(value = """
        SELECT DISTINCT sp.user_id, sp.post_id, sp.post_saved_time
        FROM saved_post sp
        JOIN post p ON p.id = sp.post_id
        JOIN user u ON u.id = sp.user_id
        JOIN post_feelings pf ON pf.post_id = p.id
        WHERE u.username = :username
        AND (pf.feeling IN (:feelings))
        AND (:day IS NULL OR DAY(sp.post_saved_time) = :day)
        AND (:month IS NULL OR MONTH(sp.post_saved_time) = :month)
        AND (:year IS NULL OR YEAR(sp.post_saved_time) = :year)
        ORDER BY sp.post_saved_time DESC
        """,
            countQuery = """
        SELECT count(sp.user_id)
        FROM saved_post sp
        JOIN post p ON p.id = sp.post_id
        JOIN user u ON u.id = sp.user_id
        JOIN post_feelings pf ON pf.post_id = p.id
        WHERE u.username = :username
        AND (pf.feeling IN (:feelings))
        AND (:day IS NULL OR DAY(sp.post_saved_time) = :day)
        AND (:month IS NULL OR MONTH(sp.post_saved_time) = :month)
        AND (:year IS NULL OR YEAR(sp.post_saved_time) = :year)
        ORDER BY sp.post_saved_time DESC
        """, nativeQuery = true,
            countProjection = "com.software.grey.models.entities.Post")
    Page<SavedPost> findSavedPostsByUsernameAndDayMonthYear(
            @Param("username") String username,
            @Param("feelings") List<String> feelings,
            @Param("day") Integer day,
            @Param("month") Integer month,
            @Param("year") Integer year,
            Pageable pageable
    );
}