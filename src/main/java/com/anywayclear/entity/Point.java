package com.anywayclear.entity;

import com.anywayclear.dto.request.PointCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private int balance = 0;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "id")
    @Column(nullable = false)
    private Member member;

    @Builder
    public Point(Long id, int balance, LocalDateTime updatedAt, Member member) {
        this.id = id;
        this.balance = balance;
        this.updatedAt = updatedAt;
        this.member = member;
    }

    public static Point toEntity(PointCreateRequest request) {
        return Point.builder()
                .balance(request.getBalance())
                .updatedAt(request.getUpdatedAt())
                .member(request.getMember())
                .build();
    }
}
