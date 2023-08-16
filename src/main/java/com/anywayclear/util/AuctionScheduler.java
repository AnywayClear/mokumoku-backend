package com.anywayclear.util;

import com.anywayclear.entity.Produce;
import com.anywayclear.repository.ProduceRepository;
import com.anywayclear.service.AlarmService;
import com.anywayclear.service.ProduceService;

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
    private final ProduceService produceService;
    private final AlarmService alarmService;

    public AuctionScheduler(ProduceRepository produceRepository, ProduceService produceService, AlarmService alarmService) {
        this.produceRepository = produceRepository;
        this.produceService = produceService;
        this.alarmService = alarmService;
    }

    @Scheduled(cron = "0 0/1 * * * ?", zone = "Asia/Seoul")
    public void updateAuctionStatus() {
        log.debug("스케줄링 시작");
        for (Produce produce : produceRepository.findAll()) {
            if (produce.getStatus() == 0 && LocalDateTime.now().isAfter(produce.getStartDate().minusMinutes(1))) {
                produce.setStatus(1);
                // 경매 시작 알림 송신
                alarmService.pushAlarm("dib", produce.getId().toString(), LocalDateTime.now());
            }
        }
        produceService.updateProduceStatus();
        log.debug("스케줄링 종료");
    }
}
