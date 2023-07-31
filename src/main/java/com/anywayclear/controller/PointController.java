package com.anywayclear.controller;

import com.anywayclear.dto.response.PointResponse;
import com.anywayclear.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> getPoint(@RequestParam(name = "userId") String userId) {
        return ResponseEntity.ok(pointService.getPoint(userId));
    }
}
