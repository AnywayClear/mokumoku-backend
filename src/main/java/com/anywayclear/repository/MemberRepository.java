package com.anywayclear.repository;

import com.anywayclear.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
}
