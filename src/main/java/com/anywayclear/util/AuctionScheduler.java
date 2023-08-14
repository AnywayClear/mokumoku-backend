package com.anywayclear.util;

import com.anywayclear.entity.Produce;
import com.anywayclear.repository.ProduceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Transactional
@Slf4j
public class AuctionScheduler {
    private final ProduceRepository produceRepository;

    public AuctionScheduler(ProduceRepository produceRepository) {
        this.produceRepository = produceRepository;
    }

    @Scheduled(cron = "0 0/1 * * * ?", zone = "Asia/Seoul")
    public void updateAuctionStatus() {
        log.debug("스케줄링 시작");
        for (Produce produce : produceRepository.findAll()) {
            if (produce.getStatus() == 0 && LocalDateTime.now().isAfter(produce.getStartDate())) {
                produce.setStatus(1);
            }
        }
    }
}
