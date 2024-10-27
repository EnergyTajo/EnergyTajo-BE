package com.energy.tajo.user.dto.response;

import com.energy.tajo.user.domain.User;
import lombok.Getter;

@Getter
public class UserCreateResponse {

    private final String uuid;
    private final String name;
    private final String email;

    private UserCreateResponse(final String uuid, final String name, final String email) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
    }

    public static UserCreateResponse from(final User user) {
        return new UserCreateResponse(user.getUuid(), user.getName(),user.getEmail());
    }
}