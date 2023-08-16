package com.anywayclear.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class SSEInMemoryRepository{
    private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    // 유실된 데이터 파악을 위한 캐시 저장 { 'userId_now' : alarm() }
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String key, SseEmitter sseEmitter) {
        sseEmitterMap.put(key, sseEmitter);
        return sseEmitter;
    }

    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    public Optional<SseEmitter> get(String key) {
        return Optional.ofNullable(sseEmitterMap.get(key));
    }

    public Map<String, Object> findAllEventCacheByUserId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, SseEmitter> findAllEmitterByUserId(String userId){
        return sseEmitterMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

//    public List<String> getKeyListByKeyPrefix(String keyPrefix){
//        return sseEmitterMap.keySet().stream()
//                .filter(key -> key.startsWith(keyPrefix))
//                .collect(Collectors.toList());
//    }

    public void delete(String key) {
        sseEmitterMap.remove(key);
    }

//    public void deleteAllByKeyPrefix(String keyPrefix) {
//        sseEmitterMap.forEach((key, emitter) -> {
//            if (key.startsWith(keyPrefix)) sseEmitterMap.remove(key);
//        });
//    }

    public void deleteAllEmitterByUserId(String userId) {
        sseEmitterMap.forEach((key, emitter) -> {
            if (key.startsWith(userId)) {
                sseEmitterMap.remove(key);
            }
        });
    }

    public void deleteAllEventCacheByUserId(String userId) {
        eventCache.forEach((key, emitter) -> {
            if (key.startsWith(userId)) {
                eventCache.remove(key);
            }
        });
    }

}
