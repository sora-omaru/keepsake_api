package com.omaru.keepsake_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KeepsakeApiApplicationTests {

    @org.springframework.beans.factory.annotation.Autowired
    com.omaru.keepsake_api.service.WorkspaceService workspaceService;
    @org.springframework.beans.factory.annotation.Autowired
    com.omaru.keepsake_api.repository.AccountRepository accountRepository;
    @org.springframework.beans.factory.annotation.Autowired
    com.omaru.keepsake_api.repository.WorkspaceRepository workspaceRepository;

    @Test
    @org.springframework.transaction.annotation.Transactional
    void workspacesArePersistedWithMembershipAndIsolatedByAccount() {
        var first = new com.omaru.keepsake_api.entity.AccountEntity();
        first.setGoogleSub(java.util.UUID.randomUUID().toString());
        first.setEmail("first@example.test");
        first = accountRepository.saveAndFlush(first);
        var second = new com.omaru.keepsake_api.entity.AccountEntity();
        second.setGoogleSub(java.util.UUID.randomUUID().toString());
        second.setEmail("second@example.test");
        second = accountRepository.saveAndFlush(second);
        var created = workspaceService.createWorkspace(first.getId(),
                new com.omaru.keepsake_api.dto.request.WorkspaceCreateRequestDto("Private"));
        org.junit.jupiter.api.Assertions.assertTrue(workspaceRepository.hasAccount(created.id(), first.getId()));
        org.junit.jupiter.api.Assertions.assertFalse(workspaceRepository.hasAccount(created.id(), second.getId()));
        org.junit.jupiter.api.Assertions.assertEquals(java.util.List.of(created), workspaceService.getWorkspaces(first.getId()));
        org.junit.jupiter.api.Assertions.assertTrue(workspaceService.getWorkspaces(second.getId()).isEmpty());
    }

	@Test
	void contextLoads() {
	}

}
