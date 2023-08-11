package com.anywayclear.controller;

import com.anywayclear.dto.request.DibCreateRequest;
import com.anywayclear.dto.response.DibResponse;
import com.anywayclear.dto.response.DibResponseList;
import com.anywayclear.dto.response.IsDibResponse;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.entity.Dib;
import com.anywayclear.service.DibService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dibs")
public class DibController {
    private final DibService dibService;

    public DibController(DibService dibService) {
        this.dibService = dibService;
    }

    @GetMapping(value = "/{userId}/subscribe", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> createDib(@PathVariable Long userId, @AuthenticationPrincipal OAuth2User oAuth2User,
                                                      @RequestHeader(value = "Last-Event_ID", required = false) String lastEventId, HttpServletResponse response) {
        return new ResponseEntity<>(dibService.createDib(userId, oAuth2User, lastEventId, LocalDateTime.now()), HttpStatus.OK);
//        return ResponseEntity.created(URI.create("api/subscribes/" + id)).build();
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

}
