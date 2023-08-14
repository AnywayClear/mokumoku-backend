package com.anywayclear.controller;

import com.anywayclear.dto.response.DibResponse;
import com.anywayclear.dto.response.IsDibResponse;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.entity.Dib;
import com.anywayclear.service.DibService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dibs")
public class DibController {
    private final DibService dibService;

    public DibController(DibService dibService) {
        this.dibService = dibService;
    }

    @PostMapping(value = "/{produceId}/dib")
    public ResponseEntity<Void> createDib(@PathVariable Long produceId, @AuthenticationPrincipal OAuth2User oAuth2User) {
        Long id = dibService.createDib(produceId, oAuth2User);
        return ResponseEntity.created(URI.create("/api/dibs/" + id)).build();
    }

    @GetMapping("/{dib-id}")
    public ResponseEntity<DibResponse> getDib(@PathVariable("dib-id") Long dibId) {
        return ResponseEntity.ok(dibService.getDib(dibId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MultiResponse<DibResponse, Dib>> getDibList(@PathVariable("userId") String userId, Pageable pageable) {
        return ResponseEntity.ok(dibService.getDibPage(userId, pageable));
    }

    @GetMapping("/{produce-id}/member")
    public ResponseEntity<IsDibResponse> getIsDib(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("produce-id") long produceId) {
        String userId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(dibService.getIsDib(userId, produceId));
    }

    @DeleteMapping("/{produce-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSubscribe(@PathVariable("produce-id") Long produceId, @AuthenticationPrincipal OAuth2User oAuth2User) {
        String consumerId = (String) oAuth2User.getAttributes().get("userId");
        dibService.deleteDib(produceId, consumerId);
    }
}
