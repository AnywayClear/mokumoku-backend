package com.anywayclear.controller;

import com.anywayclear.dto.request.DealCreateRequest;
import com.anywayclear.dto.response.DealResponse;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.entity.Deal;
import com.anywayclear.service.DealService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/deals")
public class DealController {
    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping
    public ResponseEntity<Void> createDeal(@Valid @RequestBody DealCreateRequest request) {
        final Long id = dealService.createDeal(request);
        return ResponseEntity.created(URI.create("/api/deals/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<MultiResponse<DealResponse, Deal>> getDealList(
            @RequestParam(value = "user-id") String userId,
            @RequestParam(value = "start-date", required = false) String startDateString,
            @RequestParam(value = "end-date", required = false) String endDateString,
            Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateString + " 00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString + " 00:00:00", formatter);
        return ResponseEntity.ok(dealService.getDealList(userId, startDate, endDate, pageable));
    }
}
