package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.request.EntryCreateRequestDto;
import com.omaru.keepsake_api.dto.request.EntryUpdateRequestDto;
import com.omaru.keepsake_api.entity.*;
import com.omaru.keepsake_api.repository.*;
import com.omaru.keepsake_api.service.impl.EntryServiceImpl;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntryCreatorTests {
    final WorkspaceRepository workspaces = mock(WorkspaceRepository.class);
    final TopicRepository topics = mock(TopicRepository.class);
    final AccountRepository accounts = mock(AccountRepository.class);
    final EntryRepository entries = mock(EntryRepository.class);
    final EntryServiceImpl service = new EntryServiceImpl(workspaces, topics, accounts, entries);

    EntryEntity entry() {
        WorkspaceEntity workspace = new WorkspaceEntity(); workspace.setId(1L);
        TopicEntity topic = new TopicEntity(); topic.setId(2L);
        EntryEntity entry = new EntryEntity(); entry.setId(3L); entry.setWorkspace(workspace); entry.setTopic(topic);
        when(workspaces.findById(1L)).thenReturn(Optional.of(workspace));
        when(topics.findByIdAndWorkspace_Id(2L, 1L)).thenReturn(Optional.of(topic));
        when(entries.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        return entry;
    }

    @Test void creationUsesAuthenticatedAccountWithoutMember() {
        entry();
        AccountEntity account = new AccountEntity(); account.setId(7L); account.setDisplayName("作成者A");
        when(accounts.findById(7L)).thenReturn(Optional.of(account));
        var response = service.createEntry(1L, 2L, 7L, new EntryCreateRequestDto("持ち物", "傘"));
        assertEquals(7L, response.creatorId());
        assertEquals("作成者A", response.creatorName());
        assertNull(response.memberId());
        verify(accounts).findById(7L);
    }

    @Test void editPreservesCreator() {
        EntryEntity entry = entry();
        AccountEntity account = new AccountEntity(); account.setId(7L); account.setDisplayName("作成者A"); entry.setCreator(account);
        when(entries.findByIdAndWorkspace_IdAndTopic_Id(3L, 1L, 2L)).thenReturn(Optional.of(entry));
        var response = service.updateEntry(1L, 2L, 3L, new EntryUpdateRequestDto("変更", "補足"));
        assertEquals(7L, response.creatorId());
        assertEquals("作成者A", response.creatorName());
        verifyNoInteractions(accounts);
    }

    @Test void legacyEntryRetainsOriginalMemberName() {
        EntryEntity entry = entry();
        MemberEntity member = new MemberEntity(); member.setId(4L); member.setName("以前の作成者"); entry.setMember(member);
        when(entries.findByWorkspace_IdAndTopic_Id(1L, 2L)).thenReturn(List.of(entry));
        var response = service.getEntries(1L, 2L).getFirst();
        assertNull(response.creatorId());
        assertEquals("以前の作成者", response.creatorName());
    }
    @Test void completionCanBeSetAndClearedWithoutChangingContentOrCreator() {
        EntryEntity entry = entry();
        entry.setTitle("持ち物"); entry.setContent("傘");
        AccountEntity creator = new AccountEntity(); creator.setId(7L); entry.setCreator(creator);
        when(entries.findByIdAndWorkspace_IdAndTopic_Id(3L, 1L, 2L)).thenReturn(Optional.of(entry));
        when(entries.findByWorkspace_IdAndTopic_Id(1L, 2L)).thenReturn(List.of(entry));
        assertFalse(entry.isCompleted());
        service.updateCompletion(1L, 2L, 3L, true);
        assertTrue(service.getEntries(1L, 2L).getFirst().completed());
        service.updateEntry(1L, 2L, 3L, new EntryUpdateRequestDto("更新", "補足"));
        assertTrue(entry.isCompleted());
        service.updateCompletion(1L, 2L, 3L, false);
        assertFalse(service.getEntries(1L, 2L).getFirst().completed());
        assertEquals("更新", entry.getTitle());
        assertEquals("補足", entry.getContent());
        assertSame(creator, entry.getCreator());
    }

    @Test void completionRejectsAnEntryOutsideTheRequestedList() {
        when(entries.findByIdAndWorkspace_IdAndTopic_Id(3L, 99L, 2L)).thenReturn(Optional.empty());
        assertThrows(com.omaru.keepsake_api.exception.ApiException.class,
                () -> service.updateCompletion(99L, 2L, 3L, true));
        verify(entries, never()).save(any());
    }

    @Test void completionMustBeProvided() {
        try (var factory = jakarta.validation.Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            assertFalse(validator.validate(new com.omaru.keepsake_api.dto.request.EntryCompletionRequestDto(null)).isEmpty());
            assertTrue(validator.validate(new com.omaru.keepsake_api.dto.request.EntryCompletionRequestDto(false)).isEmpty());
        }
    }
}
