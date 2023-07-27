package com.anywayclear.repository;

import com.anywayclear.entity.Produce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduceRepository extends JpaRepository<Produce, Long> {
    List<Produce> findByStatusIn(List<Integer> statusNoList);

}