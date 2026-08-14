package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
    boolean existsByWorkspaceIdAndName(Long workspaceId, String name);

    List<TopicEntity> findByWorkspace_Id(Long workspaceId);
}
