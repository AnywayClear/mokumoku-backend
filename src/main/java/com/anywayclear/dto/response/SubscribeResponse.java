package com.anywayclear.dto.response;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Subscribe;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeResponse {
    private Member consumer;
    private Member seller;

    @Builder
    public SubscribeResponse(Member consumer, Member seller) {
        this.consumer = consumer;
        this.seller = seller;
    }

    public static SubscribeResponse toResponse(Subscribe subscribe) {
        return SubscribeResponse.builder()
                .consumer(subscribe.getConsumer())
                .seller(subscribe.getSeller())
                .build();
    }
}
