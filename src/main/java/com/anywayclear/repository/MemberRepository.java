package com.anywayclear.repository;

import com.anywayclear.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAddress(String emailAddress); // NullPointerException과 같은 예외를 방지하기 위해 Optional 사용
}
