package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.EntryTagEntity;
import com.omaru.keepsake_api.entity.EntryTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryTagRepository extends JpaRepository<EntryTagEntity, EntryTagId> {
}
