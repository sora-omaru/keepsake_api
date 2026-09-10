package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.account.GoogleAccountDto;
import com.omaru.keepsake_api.entity.AccountEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.AccountRepository;
import com.omaru.keepsake_api.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountNameTests {
    final AccountRepository repository = mock(AccountRepository.class);
    final AccountServiceImpl service = new AccountServiceImpl(repository);

    @Test void registersAndUpdatesOwnName() {
        AccountEntity account = new AccountEntity(); account.setId(7L);
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        assertEquals("名前", service.updateDisplayName(7L, " 名前 ").displayName());
        assertEquals("新しい名前", service.updateDisplayName(7L, "新しい名前").displayName());
        verify(repository, times(2)).save(account);
    }
    @Test void rejectsInvalidNamesAndMissingAuthentication() {
        assertThrows(ApiException.class, () -> service.updateDisplayName(null, "名前"));
        for (String name : new String[]{null, "", "  ", "a".repeat(101)}) {
            assertThrows(ApiException.class, () -> service.updateDisplayName(7L, name));
        }
        verifyNoInteractions(repository);
    }
    @Test void googleLoginPreservesChosenName() {
        AccountEntity account = new AccountEntity(); account.setDisplayName("自分の名前");
        when(repository.findByGoogleSub("sub")).thenReturn(Optional.of(account));
        var result = service.findOrCreateByGoogle(new GoogleAccountDto("sub", "new@example.test", "Googleの名前", "picture"));
        assertEquals("自分の名前", result.getDisplayName());
        assertEquals("new@example.test", result.getEmail());
    }
    @Test void firstLoginInitializesGoogleName() {
        when(repository.findByGoogleSub("sub")).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        assertEquals("Googleの名前", service.findOrCreateByGoogle(new GoogleAccountDto("sub", "a@example.test", "Googleの名前", null)).getDisplayName());
    }
}
