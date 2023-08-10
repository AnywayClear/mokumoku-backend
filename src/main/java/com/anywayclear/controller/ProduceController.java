package com.anywayclear.controller;


import com.anywayclear.dto.request.ProduceCreateRequest;
import com.anywayclear.dto.response.AuctionResponseList;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.dto.response.ProduceResponse;
import com.anywayclear.entity.Produce;
import com.anywayclear.service.AuctionService;
import com.anywayclear.service.ProduceService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produces")
//@Secured({"ROLE_CONSUMER", "ROLE_SELLER"})
public class ProduceController {
    private final ProduceService produceService;
    private final AuctionService auctionService;

    public ProduceController(ProduceService produceService, AuctionService auctionService) {
        this.produceService = produceService;
        this.auctionService = auctionService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('ROLE_CONSUMER')")
//    @Secured({"ROLE_CONSUMER", "ROLE_SELLER"})
    public ResponseEntity<Void> createProduce(@AuthenticationPrincipal OAuth2User oAuth2User, @Valid @RequestBody ProduceCreateRequest request) {
        String sellerId = (String) oAuth2User.getAttributes().get("userId");
        final Long id = produceService.createProduce(request, sellerId);
        return ResponseEntity.created(URI.create("/api/produces/" + id)).build();
    }

    @GetMapping("/{id}")
//    @Secured({"ROLE_CONSUMER", "ROLE_SELLER"})
    public ResponseEntity<ProduceResponse> getProduce(@Positive @PathVariable("id") long id) {
        return ResponseEntity.ok(produceService.getProduce(id));
    }

    @GetMapping
    public ResponseEntity<MultiResponse<ProduceResponse, Produce>> getProduceList(@RequestParam List<Integer> statusNoList,
                                                                                  Pageable pageable) {
        return ResponseEntity.ok(produceService.getProducePage(statusNoList,pageable));
    }

    @GetMapping("/{id}/auctions")
    public ResponseEntity<AuctionResponseList> getAuctionList(@PathVariable long id) {
        return ResponseEntity.ok(auctionService.getAuctionList(id));
    }
}
