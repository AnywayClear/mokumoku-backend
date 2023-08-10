package com.anywayclear.service;

import com.anywayclear.dto.request.ReviewRequest;
import com.anywayclear.dto.response.ReviewResponse;
import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Deal;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Review;
import com.anywayclear.exception.CustomException;
import com.anywayclear.exception.ExceptionCode;
import com.anywayclear.repository.AuctionRepository;
import com.anywayclear.repository.DealRepository;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final MemberRepository memberRepository;
    private final DealRepository dealRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(MemberRepository memberRepository, DealRepository dealRepository, ReviewRepository reviewRepository) {
        this.memberRepository = memberRepository;
        this.dealRepository = dealRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Long createReview(String reviewerId, Long dealId, ReviewRequest request) {
        Review review = reviewRepository.save(Review.toEntity(request));

        Member reviewer = memberRepository.findByUserId(reviewerId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
        Deal deal = dealRepository.findById(dealId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_RESOURCE));

        review.setMember(reviewer);
        review.setDeal(deal);
        deal.setReview(review);

        return review.getId();
    }

    @Transactional
    public Long updateReview(ReviewRequest request, Long reviewId, String reviewerId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_REVIEW));

        if (!review.getMember().getUserId().equals(reviewerId)) {
            throw new CustomException(ExceptionCode.INVALID_AUTH);
        }

        review.setComment(request.getComment());
        review.setScore(request.getScore());

        return reviewId;
    }

    public Long deleteReview(Long reviewId, String reviewerId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_REVIEW));

        if (!review.getMember().getUserId().equals(reviewerId)) {
            throw new CustomException(ExceptionCode.INVALID_AUTH);
        }

        Deal deal = review.getDeal();
        System.out.println(deal);
        deal.setReview(null);

        dealRepository.save(deal);
        reviewRepository.delete(review);

        return reviewId;
    }

    @Transactional
    public ReviewResponse getReview(Long dealId) {

        Deal deal = dealRepository.findById(dealId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_DEAL));

        Review review = deal.getReview();

        if (review == null) return null;

        return ReviewResponse.toResponse(review);
    }
}
