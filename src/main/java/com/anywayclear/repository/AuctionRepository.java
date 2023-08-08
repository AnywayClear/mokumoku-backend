package com.anywayclear.repository;

import com.anywayclear.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findById(Long id);
}