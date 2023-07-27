package com.anywayclear.service;

import com.anywayclear.dto.request.SubscribeCreateRequest;
import com.anywayclear.dto.response.SubscribeResponse;
import com.anywayclear.entity.Subscribe;
import com.anywayclear.repository.SubscribeRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;

    public SubscribeService(SubscribeRepository subscribeRepository) {
        this.subscribeRepository = subscribeRepository;
    }

    public Long createSubscribe(SubscribeCreateRequest request) {
        return subscribeRepository.save(Subscribe.toEntity(request)).getId();
    }

    public SubscribeResponse getSubscribe(Long id) {
        Subscribe subscribe = subscribeRepository.findById(id).orElseThrow(() -> new RuntimeException());
        return SubscribeResponse.toResponse(subscribe);
    }
}
