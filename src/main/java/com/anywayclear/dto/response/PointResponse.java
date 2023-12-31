package com.anywayclear.dto.response;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PointResponse {
    private int balance;
    private LocalDateTime updatedAt;

    @Builder
    public PointResponse(int balance, LocalDateTime updatedAt) {
        this.balance = balance;
        this.updatedAt = updatedAt;
    }

    public static PointResponse toResponse(Point point) {
        return PointResponse.builder()
                .balance(point.getBalance())
                .updatedAt(point.getUpdatedAt())
                .build();
    }
}
