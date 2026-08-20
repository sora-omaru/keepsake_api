package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findByWorkspace_Id(Long workspace);

    Optional<TagEntity> findByIdAndWorkspace_Id(Long tagId, Long workspaceId);
}
