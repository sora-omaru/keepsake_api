package com.omaru.keepsake_api.repository;

import com.omaru.keepsake_api.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
}
