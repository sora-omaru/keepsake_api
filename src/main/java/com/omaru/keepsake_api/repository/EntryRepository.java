package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<EntryEntity,Long> {
    List<EntryEntity> findByWorkspace_IdAndTopic_Id(Long workspaceId, Long topicId);

    Optional<EntryEntity> findByIdAndWorkspace_IdAndTopic_Id(Long entryId, Long workspaceId, Long topicId);
}
