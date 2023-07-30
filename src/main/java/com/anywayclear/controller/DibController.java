package com.anywayclear.controller;

import com.anywayclear.dto.response.DibResponseList;
import com.anywayclear.service.DibService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dibs")
public class DibController {
    private final DibService dibService;

    public DibController(DibService dibService) {
        this.dibService = dibService;
    }

    @GetMapping
    public ResponseEntity<DibResponseList> getDibList(@RequestParam(name = "userId") String userId) {
        return ResponseEntity.ok(dibService.getDibList(userId));
    }

}
