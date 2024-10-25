package com.energy.tajo.card.controller;

import com.energy.tajo.card.dto.request.CardRequest;
import com.energy.tajo.card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/add_card")
    public ResponseEntity<String> addCard(
        @RequestHeader("Authorization") String token,
        @Valid @RequestBody CardRequest cardRequest) {

        cardService.addCard(token, cardRequest);
        return ResponseEntity.ok("카드가 정상적으로 등록되었습니다.");
    }
}
