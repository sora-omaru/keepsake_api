package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<EntryEntity,Long> {

}
