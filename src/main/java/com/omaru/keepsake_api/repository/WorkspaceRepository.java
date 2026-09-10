package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {
    @Query(value = "SELECT w.* FROM workspace w JOIN workspace_account wa ON wa.workspace_id = w.id WHERE wa.account_id = :accountId ORDER BY w.id", nativeQuery = true)
    List<WorkspaceEntity> findAllForAccount(@Param("accountId") Long accountId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM workspace_account WHERE workspace_id = :workspaceId AND account_id = :accountId)", nativeQuery = true)
    boolean hasAccount(@Param("workspaceId") Long workspaceId, @Param("accountId") Long accountId);

    @Modifying
    @Query(value = "INSERT INTO workspace_account (workspace_id, account_id) VALUES (:workspaceId, :accountId)", nativeQuery = true)
    void addAccount(@Param("workspaceId") Long workspaceId, @Param("accountId") Long accountId);
}
