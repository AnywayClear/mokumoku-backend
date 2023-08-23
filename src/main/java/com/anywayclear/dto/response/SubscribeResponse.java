package com.anywayclear.dto.response;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Subscribe;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeResponse {

    private String userId;
    private String image;
    private String nickName;

    @Builder
    public SubscribeResponse(String userId, String image, String nickName) {
        this.userId = userId;
        this.image = image;
        this.nickName = nickName;
    }

    public static SubscribeResponse toResponse(Member member) {
        return SubscribeResponse.builder()
                .userId(member.getUserId())
                .image(member.getImage())
                .nickName(member.getNickname())
                .build();
    }
}
