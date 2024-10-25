package com.energy.tajo.card.controller;

import com.energy.tajo.card.dto.request.CardRequest;
import com.energy.tajo.card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<String> addCard(@Valid @RequestBody CardRequest cardRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        cardService.addCard(uuid, cardRequest);

        return ResponseEntity.ok("카드가 정상적으로 등록되었습니다.");
    }
}

