package com.anywayclear.service;

import com.anywayclear.dto.response.AuctionCompleteNotification;
import com.anywayclear.entity.Auction;
import com.anywayclear.repository.EmitterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class NotificationService {
    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;

    public NotificationService(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter subscribe(String userId, String lastEventId) {
        String id = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = createEmitter(id);
        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(userId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

//    public void notifyTest(String userId, Object data) {
//        sendToClient(userId, data);
//    }

    public void notify(String userId, Auction auction) {
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(userId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, auction);
                    sendToClient(emitter, key, AuctionCompleteNotification.toResponse(auction));
                }
        );
    }


    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(id);
            emitter.completeWithError(e);
        }

    }

    private SseEmitter createEmitter(String id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);
        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        return emitter;
    }
}
