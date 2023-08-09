package com.anywayclear.controller;

import com.anywayclear.dto.request.DibCreateRequest;
import com.anywayclear.dto.response.DibResponseList;
import com.anywayclear.dto.response.IsDibResponse;
import com.anywayclear.service.DibService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/dibs")
public class DibController {
    private final DibService dibService;

    public DibController(DibService dibService) {
        this.dibService = dibService;
    }

    @PostMapping
    public ResponseEntity<Void> createDib(@Valid @RequestBody DibCreateRequest request) {
        final Long id = dibService.createDib(request);
        return ResponseEntity.created(URI.create("api/dibs/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<DibResponseList> getDibList(@RequestParam(name = "userId") String userId) {
        return ResponseEntity.ok(dibService.getDibList(userId));
    }

    @GetMapping("/{produce-id}/member")
    public ResponseEntity<IsDibResponse> getIsDib(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("produce-id") long produceId) {
        String userId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(dibService.getIsDib(userId, produceId));
    }

}
