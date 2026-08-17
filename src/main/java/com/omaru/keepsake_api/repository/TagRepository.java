package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findByWorkspace_Id(Long workspace);
}
