package com.energy.tajo.card.dto.request;

import jakarta.validation.constraints.NotNull;

public record CardRequest(
    @NotNull(message="카드 번호를 입력해주세요")
    String cardNum,

    @NotNull(message = "유효 기간을 입력해주세요")
    String validThru,

    @NotNull(message = "CVC 코드를 입력해주세요.")
    String cvc
) {


}
