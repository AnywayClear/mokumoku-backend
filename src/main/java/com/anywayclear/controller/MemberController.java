package com.anywayclear.controller;

import com.anywayclear.dto.request.MemberUpdateRequest;
import com.anywayclear.dto.response.MemberDeleteResponse;
import com.anywayclear.dto.response.MemberResponse;
import com.anywayclear.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping
    public ResponseEntity<MemberResponse> updateMember(@RequestBody MemberUpdateRequest request, @AuthenticationPrincipal OAuth2User oAuth2User){
        String userId = (String) oAuth2User.getAttributes().get("userId");
        System.out.println("userId = " + userId);
        MemberResponse updatedMember = memberService.updateMember(userId, request);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping
    public ResponseEntity<MemberDeleteResponse> deleteMember(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String userId = (String) oAuth2User.getAttributes().get("userId");
        MemberDeleteResponse deletedMember = memberService.deleteMember(userId);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(deletedMember);
    }
}
