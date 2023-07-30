package com.anywayclear.dto.response;


import com.anywayclear.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponse {
    private String userId;
    private String nickname;
    private String image;
    private String emailAddress;
    private String role;
    private String phoneNumber;
    private String description;
    private String companyRegistrationNumber;
    private String companyAddress;

    @Builder
    public MemberResponse(String userId, String nickname, String image, String emailAddress, String role,String phoneNumber, String description, String companyRegistrationNumber, String companyAddress) {
        this.userId = userId;
        this.nickname = nickname;
        this.image = image;
        this.emailAddress = emailAddress;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyAddress = companyAddress;
    }

    public static MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .image(member.getImage())
                .emailAddress(member.getEmailAddress())
                .role(member.getRole())
                .phoneNumber(member.getPhoneNumber())
                .description(member.getDescription())
                .companyRegistrationNumber(member.getCompanyRegistrationNumber())
                .companyAddress(member.getCompanyAddress())
                .build();
    }
}
