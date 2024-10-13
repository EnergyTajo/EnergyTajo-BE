package com.energy.tajo.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
    @NotBlank(message = "ID를 입력해주세요.")
    String uuid,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String pw
) {

}
