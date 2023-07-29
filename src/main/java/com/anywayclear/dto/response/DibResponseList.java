package com.anywayclear.dto.response;

import com.anywayclear.entity.Dib;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DibResponseList {
    private List<DibResponse> dibResponseList;

    public DibResponseList(final List<Dib> dibList) {
        this.dibResponseList = dibList.stream()
                .map(DibResponse::toResponse)
                .collect(Collectors.toList());
    }
}
