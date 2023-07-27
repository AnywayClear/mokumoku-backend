package com.anywayclear.entity;

import com.anywayclear.dto.request.MemberCreateRequest;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String emailAddress;

    private String phoneNumber;

    private String description;

    private String companyRegistrationNumber;

    private String companyAddress;

    @Builder
    public Member(String id, String nickname, String image, String emailAddress, String phoneNumber, String description, String companyRegistrationNumber, String companyAddress) {
        this.id = id;
        this.nickname = nickname;
        this.image = image;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyAddress = companyAddress;
    }

    public static Member toEntity(MemberCreateRequest request) {
        return Member.builder()
                .nickname(request.getNickname())
                .image(request.getImage())
                .emailAddress(request.getEmailAddress())
                .phoneNumber(request.getPhoneNumber())
                .description(request.getDescription())
                .companyRegistrationNumber(request.getCompanyRegistrationNumber())
                .companyAddress(request.getCompanyAddress())
                .build();
    }
}
