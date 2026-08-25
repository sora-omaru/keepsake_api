package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByGoogleSub(String googleSub);
}
