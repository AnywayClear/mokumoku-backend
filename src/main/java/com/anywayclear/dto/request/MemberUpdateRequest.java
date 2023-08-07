package com.anywayclear.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {

    private String nickname;
    private String image;
    private String role;
    private String phoneNumber;
    private String description;
    private String companyRegistrationNumber;
    private String companyAddress;

    @Builder
    public MemberUpdateRequest(String nickname, String image, String role, String phoneNumber, String description, String companyRegistrationNumber, String companyAddress) {
        this.nickname = nickname;
        this.image = image;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyAddress = companyAddress;
    }
}
