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
    private String consumerId;
    @NotNull
    private String sellerId;

    @Builder
    public SubscribeCreateRequest(String consumerId, String sellerId) {
        this.consumerId = consumerId;
        this.sellerId = sellerId;
    }
}
