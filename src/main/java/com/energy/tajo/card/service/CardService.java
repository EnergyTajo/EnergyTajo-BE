package com.energy.tajo.card.service;

import com.energy.tajo.card.domain.Card;
import com.energy.tajo.card.dto.request.CardRequest;
import com.energy.tajo.card.repository.CardRepository;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    // 카드 등록
    public void addCard(String uuid, CardRequest cardRequest) {
        User user = userRepository.findById(uuid)
            .orElseThrow(() -> new EnergyException(ErrorCode.USER_NOT_FOUND));

        if (cardRepository.existsByCardNum(cardRequest.cardNum())) {
            throw new EnergyException(ErrorCode.CARD_ALREADY_REGISTERED);
        }

        Card card = new Card();
        card.setUser(user);
        card.setCardNum(cardRequest.cardNum());
        card.setValidThru(cardRequest.validThru());
        card.setCvc(cardRequest.cvc());
        cardRepository.save(card);
    }
}
