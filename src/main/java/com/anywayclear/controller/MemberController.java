package com.anywayclear.controller;

import com.anywayclear.dto.request.MemberCreateRequest;
import com.anywayclear.dto.request.MemberUpdateRequest;
import com.anywayclear.dto.response.MemberResponse;
import com.anywayclear.entity.Member;
import com.anywayclear.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MemberResponse> getMemberDetail(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(memberService.getMemberByUserId(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable("userId") String userId, @RequestBody MemberUpdateRequest request){
        MemberResponse updatedMember = memberService.updateMember(userId, request);
        return ResponseEntity.ok(updatedMember);
    }
}
