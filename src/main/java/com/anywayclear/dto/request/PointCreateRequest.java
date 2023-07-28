package com.anywayclear.dto.request;

import com.anywayclear.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PointCreateRequest {

    @NotBlank
    private int balance;
    @NotBlank
    private LocalDateTime updatedAt;
    @NotBlank
    private Member member;

    @Builder
    public PointCreateRequest(int balance, LocalDateTime updatedAt, Member member) {
        this.balance = balance;
        this.updatedAt = updatedAt;
        this.member = member;
    }
}
