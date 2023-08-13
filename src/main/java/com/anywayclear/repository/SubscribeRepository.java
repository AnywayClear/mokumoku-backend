package com.anywayclear.repository;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findAllByConsumer(Member member);
    List<Subscribe> findAllBySeller(Member member);

    Optional<Subscribe> findByConsumerAndSeller(Member consumer, Member seller);

    void deleteByConsumerAndSeller(Member consumer, Member seller);
}
