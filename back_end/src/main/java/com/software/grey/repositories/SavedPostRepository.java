package com.software.grey.repositories;

import com.software.grey.models.entities.SavedPost;
import com.software.grey.models.entities.SavedPostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, SavedPostId> {
}
