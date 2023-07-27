package com.anywayclear.dto.response;

import com.anywayclear.entity.Produce;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProduceResponseList {
    private List<ProduceResponse> produceResponseList;

    public ProduceResponseList(final List<Produce> produceList) {
        this.produceResponseList = produceList.stream()
                .map((p) -> ProduceResponse.toResponse(p))
                .collect(Collectors.toList());
    }
}
