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

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    Post findByUser(User user);

    /**
     * To select posts that the user wrote and filter them by day, month and year and sort them descendingly.
     * @param userName the username of the user
     * @param day the day of the month to filter with
     * @param month the month of the year to filter with
     * @param year the year to filter with
     * @param pageable the pagination information
     * @return the page of posts the user wrote in the specified day, month and year
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



    /**
     * This query returns the feelings the user wrote about in their last 5 posts, and their frequency the inner
     * query returns the feelings in the user's last 5 posts the outer query sums those feelings and returns
     * their frequency
     * @param id the id of the user
     * @return the list of feelings and their frequency
     */
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
    List<Post> findByCollaborativeFiltering(String feeling, String userId, Pageable pageable);

    /**
     * To select the posts excluding the posts that the logged-in user wrote and filter them by feelings including any
     * post have any one of the feelings that the user specified and sort them by wrote time descendingly.
     * @param userName the username of the user
     * @param feelings the list of feelings to filter with
     * @param pageable the pagination information
     * @return the page of posts excluding the posts that the logged-in user wrote and filter them by feelings
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
}