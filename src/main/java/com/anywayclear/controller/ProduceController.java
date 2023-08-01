package com.anywayclear.controller;


import com.anywayclear.dto.request.ProduceCreateRequest;
import com.anywayclear.dto.response.ProduceResponse;
import com.anywayclear.dto.response.ProduceResponseList;
import com.anywayclear.entity.Member;
import com.anywayclear.service.ProduceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produces")
public class ProduceController {
    private final ProduceService produceService;

    public ProduceController(ProduceService produceService) {
        this.produceService = produceService;
    }

    @PostMapping
    public ResponseEntity<Void> getProduce(Authentication authentication, @Valid @RequestBody ProduceCreateRequest request) {
//        System.out.println(oAuth2User.getAttributes());
        System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());
        final Long id = produceService.createProduce(request);
        return ResponseEntity.created(URI.create("/api/produces/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduceResponse> getProduce(@Positive @PathVariable("id") long id) {
        return ResponseEntity.ok(produceService.getProduce(id));
    }

    @GetMapping
    public ResponseEntity<ProduceResponseList> getProduceList(@RequestParam List<Integer> statusNoList) {
        return ResponseEntity.ok(produceService.getProduceList(statusNoList));
    }
}
