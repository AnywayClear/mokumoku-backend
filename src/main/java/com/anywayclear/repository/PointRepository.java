package com.anywayclear.repository;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

}
