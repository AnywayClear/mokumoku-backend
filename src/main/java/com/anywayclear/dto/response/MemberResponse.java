package com.anywayclear.dto.response;


import com.anywayclear.entity.Member;
import lombok.Builder;

public class MemberResponse {
    private String nickname;
    private String image;
    private String emailAddress;
    private String phoneNumber;
    private String description;
    private String companyRegistrationNumber;
    private String companyAddress;

    @Builder
    public MemberResponse(String nickname, String image, String emailAddress, String phoneNumber, String desc, String companyRegistrationNumber, String companyAddress) {
        this.nickname = nickname;
        this.image = image;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyAddress = companyAddress;
    }

    public static MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .nickname(member.getNickname())
                .image(member.getImage())
                .emailAddress(member.getEmailAddress())
                .phoneNumber(member.getPhoneNumber())
                .desc(member.getDescription())
                .companyRegistrationNumber(member.getCompanyRegistrationNumber())
                .companyAddress(member.getCompanyAddress())
                .build();
    }
}
