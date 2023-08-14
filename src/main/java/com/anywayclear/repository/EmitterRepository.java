package com.anywayclear.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     *
     * @param id        - 사용자 아이디
     * @param emitter   - 이벤트 Emitter
     */
    public void save(String id, SseEmitter emitter) {
        emitters.put(id,emitter);
    }

    public void deleteById(String id) {
        emitters.remove(id);
    }

    public SseEmitter get(String id) {
        return emitters.get(id);
    }
}
