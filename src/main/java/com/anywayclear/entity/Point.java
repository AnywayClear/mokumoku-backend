package com.anywayclear.entity;

import com.anywayclear.dto.request.PointCreateRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Point extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private int balance = 0;

//    @Column(nullable = false)
//    private LocalDateTime updatedAt;

    @OneToOne
    @JsonManagedReference // 순환참조 방지
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public Point(Long id, int balance, Member member) {
        this.id = id;
        this.balance = balance;
//        this.updatedAt = updatedAt;
        this.member = member;
    }

    @Builder
    public Point(Member member) {
        this.member = member;
//        this.updatedAt = LocalDateTime.now();
    }

    public static Point toEntity(PointCreateRequest request) {
        return Point.builder()
                .balance(request.getBalance())
//                .updatedAt(request.getUpdatedAt())
                .member(request.getMember())
                .build();
    }
}
