package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity,Long> {
}
