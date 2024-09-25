package com.energy.tajo.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
    @NotBlank(message = "아이디를 입력해주세요.")
    String uuid,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String pw,

    @NotBlank(message = "이름를 입력해주세요.")
    String name,

    @NotBlank(message = "전화번호를 입력해주세요.")
    String phoneNum,

    @NotBlank(message = "이메일을 입력해주세요.")
    String email,

    @NotNull(message = "(필수)약관 동의 여부를 확인해주세요")
    Boolean consentStatus
    ) {

}
