package com.anywayclear.repository;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduceRepository extends JpaRepository<Produce, Long> {
    Page<Produce> findAllByStatusInAndNameContaining(List<Integer> statusNoList, Pageable pageable, String name);

    Page<Produce> findAllBySellerAndStatusInAndNameContaining(Member seller,Pageable pageable,List<Integer> statusNoList,String name);
}