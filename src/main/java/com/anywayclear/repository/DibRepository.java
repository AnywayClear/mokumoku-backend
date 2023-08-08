package com.anywayclear.repository;

import com.anywayclear.entity.Dib;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DibRepository extends JpaRepository<Dib, Long> {

    List<Dib> findAllByConsumer(Member member); // 찜 중인 농산물 찾기

    List<Dib> findAllByProduce(Produce produce); // 해당 농산물을 찜한 소비자 찾기

    Optional<Dib> findByConsumerAndProduce(Member member, Produce produce);
}
