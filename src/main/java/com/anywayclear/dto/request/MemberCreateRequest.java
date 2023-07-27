package com.anywayclear.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class MemberCreateRequest {

    @NotBlank
    private int role;
    @NotBlank
    private String nickname;
    @NotBlank
    private String image;
    @NotBlank
    private String emailAddress;
    private String phoneNumber;
    private String description;
    private String companyRegistrationNumber;
    private String companyAddress;

    @Builder
    public MemberCreateRequest(int role, String nickname, String image, String emailAddress, String phoneNumber, String description, String companyRegistrationNumber, String companyAddress) {
        this.role = role;
        this.nickname = nickname;
        this.image = image;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyAddress = companyAddress;
    }
}
