package com.anywayclear.entity;

import com.anywayclear.dto.request.SubscribeCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumer_id", referencedColumnName = "id")
    private Member consumer;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Member seller;

    @Builder
    public Subscribe(Long id, Member consumer, Member seller) {
        this.id = id;
        this.consumer = consumer;
        this.seller = seller;
    }

    public static Subscribe toEntity(SubscribeCreateRequest request) {
        return Subscribe.builder()
                .consumer(request.getConsumer())
                .seller(request.getSeller())
                .build();
    }
}
