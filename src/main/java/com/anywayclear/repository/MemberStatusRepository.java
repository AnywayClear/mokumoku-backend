package com.anywayclear.repository;

import com.anywayclear.entity.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, String> {
}
