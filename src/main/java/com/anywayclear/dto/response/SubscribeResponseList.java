package com.anywayclear.dto.response;

import com.anywayclear.entity.Subscribe;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscribeResponseList {
    private List<SubscribeResponse> subscribeResponseList;

    public SubscribeResponseList(final List<Subscribe> subscribeList) {
        this.subscribeResponseList = subscribeList.stream()
                .map(SubscribeResponse::toResponse)
                .collect(Collectors.toList());
    }
}
