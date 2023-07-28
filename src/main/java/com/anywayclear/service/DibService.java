package com.anywayclear.service;

import com.anywayclear.dto.request.DibCreateRequest;
import com.anywayclear.dto.response.DibResponse;
import com.anywayclear.entity.Dib;
import com.anywayclear.repository.DibRepository;
import org.springframework.stereotype.Service;

@Service
public class DibService {
    private final DibRepository dibRepository;

    public DibService(DibRepository dibRepository) {
        this.dibRepository = dibRepository;
    }

    public Long createDib(DibCreateRequest request) {
        return dibRepository.save(Dib.toEntity(request)).getId();
    }

    public DibResponse getDib(Long id) {
        Dib dib = dibRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        return DibResponse.toResponse(dib);
    }
}
