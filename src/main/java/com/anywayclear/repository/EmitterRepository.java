package com.anywayclear.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    /**
     *
     * @param id        - 사용자 아이디
     * @param emitter   - 이벤트 Emitter
     */
    public void save(String id, SseEmitter emitter) {
        emitters.put(id,emitter);
    }

    public void saveEventCache(String id, Object event) {
        eventCache.put(id, event);
    }

    public Map<String, SseEmitter> findAllStartWithById(String id) {
        return emitters
                .entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public Map<String, Object> findAllEventCacheStartWithId(String id) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteAllStartWithId(String id) {
        emitters.forEach(
                (key,emitter)->{
                    if (key.startsWith(id)) {
                        emitters.remove(key);
                    }
                }
        );
    }
    public void deleteById(String id) {
        emitters.remove(id);
    }

    public void deleteAllEventCacheStartWithId(String id) {
        eventCache.forEach(
                (key, data) -> {
                    if (key.startsWith(id)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
    public SseEmitter get(String id) {
        return emitters.get(id);
    }
}
