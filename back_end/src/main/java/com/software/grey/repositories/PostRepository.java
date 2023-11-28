package com.software.grey.repositories;

import com.software.grey.models.entities.Post;
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
//    Page<Post> findAllByUser_Username(Pageable pageable, String userName);
//
////    @Query(value = "SELECT * FROM post e WHERE DAY(e.post_time) = :day AND MONTH(e.post_time) = :month AND YEAR(e.post_time) = :year", nativeQuery = true)
////    List<Post> findByDayMonthYear(@Param("day") int day, @Param("month") int month, @Param("year") int year);
//
//    @Query(value = "SELECT * FROM post e WHERE DAY(e.post_time) = :day AND MONTH(e.post_time) = :month AND YEAR(e.post_time) = :year", nativeQuery = true)
//    Page<Post> findByDayMonthYear(
//            @Param("day") int day,
//            @Param("month") int month,
//            @Param("year") int year,
//            Pageable pageable
//    );

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
}
