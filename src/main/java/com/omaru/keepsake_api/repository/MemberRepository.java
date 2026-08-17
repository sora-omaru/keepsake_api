package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.MemberEntity;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByWorkspaceIdAndName(Long workspaceId, String name);

    List<MemberEntity> findByWorkspace_Id(Long workspaceId);

    Optional<MemberEntity> findByIdAndWorkspace_Id(Long memberId, Long workspaceId);
}
