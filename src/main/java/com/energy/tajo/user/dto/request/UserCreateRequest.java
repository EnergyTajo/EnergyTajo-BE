package com.energy.tajo.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
    @NotBlank(message = "아이디를 입력해주세요.")
    String uuid,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String pw,

    @NotBlank(message = "이름를 입력해주세요.")
    String name,

    @NotBlank(message = "전화번호를 입력해주세요.")
    String tell,

    @NotBlank(message = "이메일을 입력해주세요.")
    String email
    ) {

}
