package com.anywayclear.util;

import com.anywayclear.entity.Produce;
import com.anywayclear.repository.ProduceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Transactional
public class AuctionScheduler {
    private final ProduceRepository produceRepository;

    public AuctionScheduler(ProduceRepository produceRepository) {
        this.produceRepository = produceRepository;
    }

    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Seoul")
    public void updateAuctionStatus() {
        for (Produce produce : produceRepository.findAll()) {
            if (produce.getStatus() == 0 && LocalDateTime.now().isAfter(produce.getStartDate())) {
                produce.setStatus(1);
            }
        }
    }
}
