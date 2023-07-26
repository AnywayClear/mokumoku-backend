package com.anywayclear.repository;

import com.anywayclear.entity.Produce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduceRepository extends JpaRepository<Produce, Long> {
}