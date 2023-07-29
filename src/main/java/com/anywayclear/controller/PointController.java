package com.anywayclear.controller;

import com.anywayclear.dto.request.PointCreateRequest;
import com.anywayclear.dto.response.PointResponse;
import com.anywayclear.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> getPoint(@RequestParam(name = "nickname") String nickname) {
        return ResponseEntity.ok(pointService.getPoint(nickname));
    }
}
