package com.anywayclear.controller;

import com.anywayclear.dto.request.DealCreateRequest;
import com.anywayclear.dto.response.DealResponseList;
import com.anywayclear.service.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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
    public ResponseEntity<DealResponseList> getDealList(@RequestParam(name = "nickname") String nickname) {
        return ResponseEntity.ok(dealService.getDealList(nickname));
    }
}
