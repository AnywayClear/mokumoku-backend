package com.anywayclear.entity;

import com.anywayclear.dto.request.MemberCreateRequest;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @Column(nullable = false)
    private String id; // Entity key

    @Column(nullable = false)
    private String userId; // 사용자 식별용 ID

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private String role; // ROLE_CONSUMER, ROLE_SELLER

    private String nickname;

    private String phoneNumber;

    private String description;

    private String companyRegistrationNumber;

    private String companyAddress;



    @Builder
    public Member(String id, String userId, String nickname, String image, String emailAddress, String role, String phoneNumber, String description, String companyRegistrationNumber, String companyAddress) {
        this.id = id;
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

    public static Member toEntity(MemberCreateRequest request) {
        return Member.builder()
                .id(request.getId())
                .userId(request.getUserId())
                .nickname(request.getNickname())
                .image(request.getImage())
                .emailAddress(request.getEmailAddress())
                .role(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .description(request.getDescription())
                .companyRegistrationNumber(request.getCompanyRegistrationNumber())
                .companyAddress(request.getCompanyAddress())
                .build();
    }
}
