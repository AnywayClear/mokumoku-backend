package com.anywayclear.controller;

import com.anywayclear.dto.request.PointUpdateRequest;
import com.anywayclear.dto.response.PointResponse;
import com.anywayclear.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PointResponse> getPoint(@PathVariable String userId) {
        return ResponseEntity.ok(pointService.getPoint(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<PointResponse> updatePoint(@PathVariable String userId, @Valid @RequestBody PointUpdateRequest request) {
        return ResponseEntity.ok(pointService.updatePoint(userId, request));
    }
}
