package com.anywayclear.entity;

import com.anywayclear.dto.request.DibCreateRequest;
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
        name = "dib",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueConsumerAndProduce",
                        columnNames = {
                                "consumer_id",
                                "produce_id"
                        }
                )
        }
)
public class Dib {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumer_id", referencedColumnName = "id")
    private Member consumer;

    @ManyToOne
    @JoinColumn(name = "produce_id", referencedColumnName = "id")
    private Produce produce;

    @Builder
    public Dib(Member consumer, Produce produce) {
        this.consumer = consumer;
        this.produce = produce;
    }

}
