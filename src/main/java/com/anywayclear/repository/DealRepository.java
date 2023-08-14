package com.anywayclear.repository;

import com.anywayclear.entity.Deal;
import com.anywayclear.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DealRepository extends JpaRepository<Deal, Long> {

    Page<Deal> findAllByConsumer(Member member, Pageable pageable);

    Page<Deal> findAllBySeller(Member member, Pageable pageable);

    Page<Deal> findAllByConsumerAndProduce_EndDateBetween(
            Member member,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable);

    Page<Deal> findAllBySellerAndProduce_EndDateBetween(
            Member member,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable);
}
