package com.anywayclear.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "Member_Status")
public class MemberStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_status_id")
    private Long id;

    @Column(nullable = false)
    private boolean isOut = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference // 순환참조 방지
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public MemberStatus(Member member) {
        this.member = member;
    }
}
