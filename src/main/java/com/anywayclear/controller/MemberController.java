package com.anywayclear.controller;

import com.anywayclear.dto.request.MemberCreateRequest;
import com.anywayclear.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> getMember(@Valid @RequestBody MemberCreateRequest request) {
        final String id = memberService.createMember(request);
        return ResponseEntity.created(URI.create("api/members/" + id)).build();
    }
}
