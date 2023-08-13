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
@Table(
        name = "subscribe",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueConsumerAndSeller",
                        columnNames = {
                                "consumer_id",
                                "seller_id"
                        }
                )
        }
)
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
    public Subscribe(Member consumer, Member seller) {
        this.consumer = consumer;
        this.seller = seller;
    }

}
