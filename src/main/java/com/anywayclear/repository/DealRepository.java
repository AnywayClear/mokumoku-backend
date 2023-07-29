package com.anywayclear.repository;

import com.anywayclear.entity.Deal;
import com.anywayclear.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findAllByConsumer(Member member);

    List<Deal> findAllBySeller(Member member);
}
