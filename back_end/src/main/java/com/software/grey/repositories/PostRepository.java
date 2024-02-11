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
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {


    Post findByUser(User user);

    /*
        To select posts that the user wrote and filter them by day, month and year and sort them descendingly.
     */
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

    // This query returns the feelings the user wrote about in their last 5 posts, and their frequency
    // the inner query returns the feelings in the user's last 5 posts
    // the outer query sums those feelings and returns their frequency
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
    List<FeelingCountProjection> findCountOfFeelingsByUser(String id);

    List<Post> findByPostFeelingsAndUserIdNot(Feeling feeling, String userId, Pageable pageable);

    @Query(value = """
            WITH FeelingPosts AS (
                SELECT
                    p.id AS post_id,
                    p.user_id,
                    pf.feeling,
                    p.post_time
                FROM
                    post p
                    JOIN post_feelings pf ON p.id = pf.post_id
                WHERE
                    pf.feeling = ?1
            ),
            UserPosts AS (
                SELECT
                    p.id,
                    p.user_id,
                    pf.feeling,
                    p.post_time,
                    p.text
                FROM
                    post p
                    JOIN post_feelings pf ON p.id = pf.post_id
            )
            SELECT DISTINCT
                up.id,
                up.text,
                up.user_id,
                up.post_time,
                up.feeling
            FROM
                UserPosts up
                WHERE up.user_id in (SELECT DISTINCT user_id FROM FeelingPosts) AND up.feeling != ?1 AND up.user_id != ?2
            ORDER BY up.post_time DESC;
            """, nativeQuery = true)
    List<Post> findByCollaborativeFiltering(String feeling, String userId,  Pageable pageable);

    /*
        To select the posts excluding the posts that the logged-in user wrote and filter them by
        feelings including any post have any one of the feelings that the user specified
        and sort them by wrote time descendingly.
     */
    @Query(value = """
            SELECT DISTINCT p.id, p.text, p.post_time, u.id as user_id
            FROM post p
            JOIN user u ON u.id = p.user_id
            JOIN post_feelings pf ON pf.post_id = p.id
            WHERE u.username != :userName
              AND (pf.feeling IN (:feelings))
            ORDER BY p.post_time DESC
            """,
            countQuery = """
    SELECT count(p.id) FROM post p JOIN user u ON u.id = p.user_id
    JOIN post_feelings pf ON pf.post_id = p.id
    WHERE u.username != :userName AND (pf.feeling IN (:feelings))
    """, nativeQuery = true)
    Page<Post> findFeed(@Param("userName") String userName, @Param("feelings") List<String> feelings, Pageable pageable);

    /*
        To select the posts excluding the posts that the logged-in user wrote and filter them by
        feelings including any post have any one of the feelings that the user specified
        and sort them by writing time descendingly.
     */
    @Query(value = """
            SELECT post_feelings.feeling, COUNT(post_feelings.feeling) AS feelingCount 
            FROM post_feelings
            JOIN post ON post.id = post_feelings.post_id
            join user ON user.id = post.user_id
            WHERE user.id = :user_id
                AND DATE(post.post_time) = CURRENT_DATE
            GROUP BY post_feelings.feeling
            ORDER BY feelingCount DESC; 
            """,
            nativeQuery = true)
    List<FeelingCountProjection> findFeelingsFrequencyForSpecificUser(@Param("user_id") String user_id);

    /*
        To select the feelings and their frequency in the posts of all users and sort them by frequency descendingly.
     */
    @Query(value = """
            SELECT feeling, COUNT(feeling) AS feelingCount
            FROM post_feelings
            JOIN post ON post.id = post_feelings.post_id
            WHERE DATE(post.post_time) = CURRENT_DATE
            GROUP BY feeling
            ORDER BY feelingCount DESC;
            """,
            nativeQuery = true)
    List<FeelingCountProjection> findGlobalFeelingFrequency();

    @Query(value = """
            SELECT COUNT(post.id) FROM post
            WHERE DATE(post_time) = CURRENT_DATE
            """,
            nativeQuery = true)
    long countPostsToday();

    @Query(value = """
            SELECT COUNT(post.id) FROM post
            WHERE DATE(post_time) = CURRENT_DATE
            AND user_id = :user_id
            """,
            nativeQuery = true)
    long countPostsTodayByUserId(@Param("user_id") String user_id);
}