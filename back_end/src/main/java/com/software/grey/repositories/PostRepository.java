package com.software.grey.repositories;

import com.software.grey.models.entities.Post;

import com.software.grey.models.entities.User;
import com.software.grey.models.enums.Feeling;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
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
}
