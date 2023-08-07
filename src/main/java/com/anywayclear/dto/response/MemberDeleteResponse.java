package com.anywayclear.dto.response;

import com.anywayclear.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
public class MemberDeleteResponse {

    private String userId; // 사용자 식별용 ID
    private String emailAddress;
    private String role; // ROLE_CONSUMER, ROLE_SELLER
    private boolean isDeleted;

    @Builder
    public MemberDeleteResponse(String userId, String emailAddress, String role, boolean isDeleted) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.role = role;
        this.isDeleted = isDeleted;
    }

    public static MemberDeleteResponse toResponse(Member member) {
        return MemberDeleteResponse.builder()
                .userId(member.getUserId())
                .emailAddress(member.getEmailAddress())
                .role(member.getRole())
                .isDeleted(member.isDeleted())
                .build();
    }
}
