package com.anywayclear.dto.response;

import com.anywayclear.entity.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewResponseList {
    private List<ReviewResponse> reviewResponseList;

    private ReviewResponseList(final List<Review> reviewList) {
        this.reviewResponseList = reviewList.stream()
                .map(ReviewResponse::toResponse)
                .collect(Collectors.toList());
    }
}
