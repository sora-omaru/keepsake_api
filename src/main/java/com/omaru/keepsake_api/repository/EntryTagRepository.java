package com.omaru.keepsake_api.repository;

// Entry一覧のTagを一括取得する場合に使用する。
// import com.omaru.keepsake_api.dto.EntryTagProjection;
import com.omaru.keepsake_api.entity.EntryTagEntity;
import com.omaru.keepsake_api.entity.EntryTagId;
import com.omaru.keepsake_api.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

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
    List<TagEntity> findTagsByEntryId(Long entryId);

    // Entry一覧へTagを含める場合の一括取得クエリ。
    // Entryごとの検索を避け、全EntryのTagを1クエリで取得する。
//    @Query("""
//            SELECT
//             et.entry.id AS entryId,
//             et.tag.id AS tagId,
//             et.tag.name AS tagName,
//             et.tag.workspace.id AS workspaceId
//            FROM EntryTagEntity et
//            WHERE et.workspace.id = :workspaceId
//            AND et.entry.id IN :entryIds
//            ORDER BY et.entry.id , et.tag.id
//            """)
//    List<EntryTagProjection> findTagsByEntryIds(
//            @Param("workspaceId") Long workspaceId,
//            @Param("entryIds") List<Long> entryIds
//    );
}
