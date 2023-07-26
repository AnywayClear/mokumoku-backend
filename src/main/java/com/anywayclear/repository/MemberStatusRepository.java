package com.anywayclear.repository;

import com.anywayclear.entity.MemberStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStatusRepository extends JpaRepository<MemberStatusEntity, String> {
}
