package com.anywayclear.dto.response;

import com.anywayclear.entity.Dib;
import com.anywayclear.entity.Produce;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DibResponseList {
    private Page<DibResponse> dibResponseList;

    public DibResponseList(final List<Produce> dibList, Pageable pageable) {
        this.dibResponseList = new PageImpl<>(dibList.stream()
                .map(DibResponse::toResponse)
                .collect(Collectors.toList()),
                pageable,
                dibList.size());
    }
}
