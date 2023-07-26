package com.anywayclear.entity;

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
@Table(name = "Member")
public class MemberEntity {

    @Id
    @Column(name = "member_id")
    private String id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "desc")
    private String desc;

    @Column(name = "company_registration_number")
    private String companyRegistrationNumber;

    @Column(name = "company_address")
    private String companyAddress;
}
