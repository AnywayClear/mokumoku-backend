package com.anywayclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SSERepository {
    void put(String emitterId, SseEmitter sseEmitter);

    Optional<SseEmitter> get(String key);

    List<SseEmitter> getListByKeyPrefix(String keyPrefix);

    List<String> getKeyListByKeyPrefix(String keyPrefix);

    void remove(String key);

    void deleteAllByKeyPrefix(String keyPrefix);
}
