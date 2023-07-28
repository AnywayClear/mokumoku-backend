package com.anywayclear.dto.request;

import com.anywayclear.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PointCreateRequest {

    @NotNull
    private int balance;
    @NotNull
    private LocalDateTime updatedAt;
    @NotNull
    private Member member;

    @Builder
    public PointCreateRequest(int balance, LocalDateTime updatedAt, Member member) {
        this.balance = balance;
        this.updatedAt = updatedAt;
        this.member = member;
    }
}
