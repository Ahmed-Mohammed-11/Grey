package com.software.grey.repositories;

import com.software.grey.models.entities.ReportedPost;
import com.software.grey.models.entities.ReportedPostId;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ReportedPostRepository extends JpaRepository<ReportedPost, ReportedPostId> {
  
    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM reported_posts
            WHERE post_id = ?1
            """, nativeQuery = true)
    int deleteByPostId(String postId);
    boolean existsByPostId(String postId);

    public Page<ReportedPost> findByOrderByPostPostTimeDesc(Pageable page);
}
