package com.anywayclear.dto.request;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class DibCreateRequest {

    @NotBlank
    private Member consumer;
    @NotBlank
    private Produce produce;

    @Builder
    public DibCreateRequest(Member consumer, Produce produce) {
        this.consumer = consumer;
        this.produce = produce;
    }
}
