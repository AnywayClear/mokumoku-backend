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
    public Dib(Long id, Member consumer, Produce produce) {
        this.id = id;
        this.consumer = consumer;
        this.produce = produce;
    }

    public static Dib toEntity(DibCreateRequest request) {
        return Dib.builder()
                .consumer(request.getConsumer())
                .produce(request.getProduce())
                .build();
    }
}
