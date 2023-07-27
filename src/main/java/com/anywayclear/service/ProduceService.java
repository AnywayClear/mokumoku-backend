package com.anywayclear.service;

import com.anywayclear.dto.request.ProduceCreateRequest;
import com.anywayclear.dto.response.ProduceResponseList;
import com.anywayclear.dto.response.ProduceResponse;
import com.anywayclear.entity.Produce;
import com.anywayclear.repository.ProduceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduceService {
    private final ProduceRepository produceRepository;

    public ProduceService(ProduceRepository produceRepository) {
        this.produceRepository = produceRepository;
    }

    public Long createProduce(ProduceCreateRequest request) {
        return produceRepository.save(Produce.toEntity(request)).getId();
    }

    public ProduceResponse getProduce(Long id) {
        Produce produce = produceRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        return ProduceResponse.toResponse(produce);
    }
    public ProduceResponseList getProduceList(List<Integer> statusNoList) {
        List<Produce> produceList = produceRepository.findByStatusIn(statusNoList);
        return new ProduceResponseList(produceList);
    }
}
