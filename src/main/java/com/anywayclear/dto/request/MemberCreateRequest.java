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
    private String nickname;
    @NotBlank
    private String image;
    @NotBlank
    private String emailAddress;
    @NotBlank
    private String role;
    @NotBlank
    private boolean isDeleted;
    private String phoneNumber;
    private String description;
    private String companyRegistrationNumber;
    private String companyAddress;

    @Builder
    public MemberCreateRequest(String nickname, String image, String emailAddress, String role,String phoneNumber, String description, String companyRegistrationNumber, String companyAddress, boolean isDeleted) {
        this.nickname = nickname;
        this.image = image;
        this.emailAddress = emailAddress;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyAddress = companyAddress;
        this.isDeleted = isDeleted;
    }
}