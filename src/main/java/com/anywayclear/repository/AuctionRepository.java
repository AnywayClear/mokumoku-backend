package com.anywayclear.repository;

import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Produce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findById(Long id);

    List<Auction> findAllByProduce(Produce produce);
}