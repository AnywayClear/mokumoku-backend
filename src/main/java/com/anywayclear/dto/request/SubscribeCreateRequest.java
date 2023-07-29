package com.anywayclear.dto.request;

import com.anywayclear.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SubscribeCreateRequest {
    @NotNull
    private Member consumer;
    @NotNull
    private Member seller;

    @Builder
    public SubscribeCreateRequest(Member consumer, Member seller) {
        this.consumer = consumer;
        this.seller = seller;
    }
}
