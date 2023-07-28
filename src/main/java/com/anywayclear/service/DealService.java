package com.anywayclear.service;

import com.anywayclear.dto.request.DealCreateRequest;
import com.anywayclear.dto.response.DealResponse;
import com.anywayclear.entity.Deal;
import com.anywayclear.repository.DealRepository;
import org.springframework.stereotype.Service;

@Service
public class DealService {
    private final DealRepository dealRepository;

    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    public Long createDeal(DealCreateRequest request) {
        return dealRepository.save(Deal.toEntity(request)).getId();
    }

    public DealResponse getDeal(Long id) {
        Deal deal = dealRepository.findById(id).orElseThrow(() -> new RuntimeException());
        return DealResponse.toResponse(deal);
    }
}
