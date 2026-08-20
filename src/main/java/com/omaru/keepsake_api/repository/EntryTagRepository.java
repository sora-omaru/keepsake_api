package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.EntryTagEntity;
import com.omaru.keepsake_api.entity.EntryTagId;
import com.omaru.keepsake_api.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntryTagRepository extends JpaRepository<EntryTagEntity, EntryTagId> {
    boolean existsByEntry_IdAndTag_Id(
            Long entryId,
            Long tagId
    );

    void deleteByEntry_IdAndTag_Id(Long entryId, Long tagId);

    @Query("""
                SELECT et.tag
                FROM EntryTagEntity et
                WHERE et.entry.id = :entryId
            """)
    List<TagEntity> findTagsByEntryUd(Long entryId);
}
