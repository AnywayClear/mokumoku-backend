package com.anywayclear.service;

import com.anywayclear.dto.request.PointUpdateRequest;
import com.anywayclear.dto.response.PointResponse;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Point;
import com.anywayclear.exception.CustomException;
import com.anywayclear.exception.ExceptionCode;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {
    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    public PointService(PointRepository pointRepository, MemberRepository memberRepository) {
        this.pointRepository = pointRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public PointResponse getPoint(String userId) {
        // 닉네임으로 검색해야 하기 때문에 멤버 리포지토리에서 멤버 객체를 불러옴
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
        // 불러온 멤버 객체에서 포인트 객체 가져오기
        Point point = member.getPoint();
        return PointResponse.toResponse(point);
    }

    @Transactional
    public PointResponse updatePoint(String userId, PointUpdateRequest request) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
        // 불러온 멤버 객체에서 포인트 객체 가져오기
        Point point = member.getPoint();
        point.setBalance(request.getBalance());
        return PointResponse.toResponse(pointRepository.save(point));
    }
}
