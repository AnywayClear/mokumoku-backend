package com.anywayclear.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MultiResponse<T,P> {
    private final List<T> data;
    private final PageInfo pageInfo;

    public MultiResponse(List<T> data, Page<P> page) {
        this.data = data;
        this.pageInfo = new PageInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
