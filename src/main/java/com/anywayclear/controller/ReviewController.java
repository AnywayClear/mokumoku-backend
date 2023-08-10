package com.anywayclear.controller;

import com.anywayclear.dto.request.ReviewCreateRequest;
import com.anywayclear.dto.response.ReviewResponse;
import com.anywayclear.entity.Member;
import com.anywayclear.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{auctionId}")
    public ResponseEntity<ReviewResponse> createReview(
            @RequestBody ReviewCreateRequest request,
            @PathVariable("auctionId") Long auctionId,
            @AuthenticationPrincipal OAuth2User oAuth2User){
        String reviewerId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(reviewService.createReview(reviewerId, auctionId, request));
    }
}
