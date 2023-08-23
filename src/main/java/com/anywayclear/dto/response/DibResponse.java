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
    // 이미지 url, 판매자이름, 판매자 식별 (닉네임 or ID 판매자 타인페이지 이동용), 제목, 경매 ID, 시작금액
    private Long id;
    private String title;
    private int startPrice;
    private String image;
    private String sellerName;
    private String userId;

    @Builder
    public DibResponse(Long id, String title, int startPrice, String image, String sellerName, String userId) {
        this.id = id;
        this.title = title;
        this.startPrice = startPrice;
        this.image = image;
        this.sellerName = sellerName;
        this.userId = userId;
    }

    public static DibResponse toResponse(Dib dib) {
        return DibResponse.builder()
                .id(dib.getProduce().getId())
                .title(dib.getProduce().getName())
                .startPrice(dib.getProduce().getStartPrice())
                .image(dib.getProduce().getImage())
                .sellerName(dib.getProduce().getSeller().getNickname())
                .userId(dib.getProduce().getSeller().getUserId())
                .build();
    }
}
