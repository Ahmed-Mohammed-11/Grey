package com.software.grey.repositories;


import com.software.grey.models.entities.ReportedPost;
import com.software.grey.models.entities.ReportedPostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedPostRepository extends JpaRepository<ReportedPost, ReportedPostId> {
}
