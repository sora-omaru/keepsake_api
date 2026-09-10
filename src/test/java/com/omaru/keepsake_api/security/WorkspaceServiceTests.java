package com.omaru.keepsake_api.security;

import com.omaru.keepsake_api.dto.request.WorkspaceCreateRequestDto;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import com.omaru.keepsake_api.repository.WorkspaceRepository;
import com.omaru.keepsake_api.service.impl.WorkspaceServiceImpl;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkspaceServiceTests {
    @Test void creationRegistersCreatorAndListingUsesAccount() {
        var repository = mock(WorkspaceRepository.class);
        var service = new WorkspaceServiceImpl(repository);
        when(repository.saveAndFlush(any())).thenAnswer(invocation -> {
            WorkspaceEntity workspace = invocation.getArgument(0);
            workspace.setId(12L);
            return workspace;
        });
        var created = service.createWorkspace(7L, new WorkspaceCreateRequestDto("  Home  "));
        assertEquals(12L, created.id());
        assertEquals("Home", created.name());
        verify(repository).addAccount(12L, 7L);
        when(repository.findAllForAccount(8L)).thenReturn(List.of());
        assertTrue(service.getWorkspaces(8L).isEmpty());
        verify(repository).findAllForAccount(8L);
    }
}
