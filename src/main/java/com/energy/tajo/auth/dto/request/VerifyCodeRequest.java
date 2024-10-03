package com.energy.tajo.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyCodeRequest(
    @NotBlank(message = "전화번호를 입력해주세요.")
    String phoneNum,

    @NotBlank(message = "ID를 입력해주세요.")
    String uuid
){

}
