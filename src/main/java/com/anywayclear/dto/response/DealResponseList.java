package com.anywayclear.dto.response;

import com.anywayclear.entity.Deal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DealResponseList {
    private List<DealResponse> dealResponsesList;

    public DealResponseList(final List<Deal> dealList) {
        this.dealResponsesList = dealList.stream()
                .map(DealResponse::toResponse)
                .collect(Collectors.toList());
    }
}
