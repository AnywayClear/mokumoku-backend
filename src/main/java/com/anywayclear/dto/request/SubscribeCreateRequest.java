package com.anywayclear.dto.request;

import com.anywayclear.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class SubscribeCreateRequest {
    @NotBlank
    private Member consumer;
    @NotBlank
    private Member seller;

    @Builder
    public SubscribeCreateRequest(Member consumer, Member seller) {
        this.consumer = consumer;
        this.seller = seller;
    }
}
