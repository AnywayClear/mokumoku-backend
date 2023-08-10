package com.anywayclear.dto.request;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DealCreateRequest {
    private int endPrice;
    private Member consumer;
    private Member seller;
    private Produce produce;
}
