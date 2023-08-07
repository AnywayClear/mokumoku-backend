package com.anywayclear.entity;

import com.anywayclear.dto.request.MemberCreateRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)
    private boolean isDeleted;

    private String nickname;

    private String phoneNumber;

    private String description;

    private String companyRegistrationNumber;

    private String companyAddress;


    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL) // 영속성 전이가 발생해 부모객체를 저장할 때 자식객체도 함께 저장
    @JsonBackReference // 순환참조 방지
    private Point point = new Point(this); // 멤버 생성 시 포인트 객체 자동 생성

    @Builder
    public Member(String id, String userId, String nickname, String image, String emailAddress, String role, String phoneNumber, String description, String companyRegistrationNumber, String companyAddress, boolean isDeleted) {
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
        this.isDeleted = isDeleted;
    }

    public static Member toEntity(MemberCreateRequest request) {
        return Member.builder()
                .nickname(request.getNickname())
                .image(request.getImage())
                .emailAddress(request.getEmailAddress())
                .role(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .description(request.getDescription())
                .companyRegistrationNumber(request.getCompanyRegistrationNumber())
                .companyAddress(request.getCompanyAddress())
                .isDeleted(request.isDeleted())
                .build();
    }
}
