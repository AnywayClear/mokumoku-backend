package com.anywayclear.service;

import com.anywayclear.dto.request.ReviewCreateRequest;
import com.anywayclear.dto.response.ReviewResponse;
import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Review;
import com.anywayclear.exception.CustomException;
import com.anywayclear.exception.ExceptionCode;
import com.anywayclear.repository.AuctionRepository;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final MemberRepository memberRepository;
    private final AuctionRepository auctionRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(MemberRepository memberRepository, AuctionRepository auctionRepository, ReviewRepository reviewRepository) {
        this.memberRepository = memberRepository;
        this.auctionRepository = auctionRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public ReviewResponse createReview(String reviewerId, Long auctionId, ReviewCreateRequest request) {
        Review review = reviewRepository.save(Review.toEntity(request));

        Member reviewer = memberRepository.findByUserId(reviewerId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_AUCTION_ID));

        review.setMember(reviewer);
        review.setAuction(auction);

        reviewRepository.save(review);
        return ReviewResponse.toResponse(review);
    }
}
