package com.anywayclear.dto.response;

import com.anywayclear.entity.Dib;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DibResponse {
    private Member consumer;
    private Produce produce;

    @Builder
    public DibResponse(Member consumer, Produce produce) {
        this.consumer = consumer;
        this.produce = produce;
    }

    public static DibResponse toResponse(Dib dib) {
        return DibResponse.builder()
                .consumer(dib.getConsumer())
                .produce(dib.getProduce())
                .build();
    }
}
