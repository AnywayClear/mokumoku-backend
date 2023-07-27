package com.anywayclear.service;

import com.anywayclear.dto.request.PointCreateRequest;
import com.anywayclear.dto.response.PointResponse;
import com.anywayclear.entity.Point;
import com.anywayclear.repository.PointRepository;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Long createPoint(PointCreateRequest request) {
        return pointRepository.save(Point.toEntity(request)).getId();
    }

    public PointResponse getPoint(Long id) {
        Point point = pointRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        return PointResponse.toResponse(point);
    }
}
