package com.energy.tajo.account.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequest (
    @JsonProperty("user_id")
    @NotBlank(message = "유저 아이디를 입력해주세요.")
    String userId,

    @JsonProperty("account_num")
    @NotBlank(message = "계좌번호를 입력해주세요.")
    String accountNum,

    @JsonProperty("bank_name")
    @NotBlank(message = "은행명을 입력해주세요.")
    String bankName,

    @JsonProperty("amount")
    @NotNull(message = "금액을 입력해주세요.")
    Integer amount
    ){
}