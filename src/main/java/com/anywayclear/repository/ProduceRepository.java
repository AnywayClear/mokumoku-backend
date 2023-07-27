package com.anywayclear.repository;

import com.anywayclear.entity.Produce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProduceRepository extends JpaRepository<Produce, Long> {
    List<Produce> findByStatusIn(List<Integer> statusNoList);

}