package com.omaru.keepsake_api.security;

import com.omaru.keepsake_api.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("workspaceAccess")
@RequiredArgsConstructor
public class WorkspaceAccess {
    private final WorkspaceRepository repository;

    public boolean isMember(Authentication authentication, Long workspaceId) {
        return authentication != null && authentication.getPrincipal() instanceof Long accountId
                && workspaceId != null && repository.hasAccount(workspaceId, accountId);
    }
}
