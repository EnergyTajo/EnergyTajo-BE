package com.energy.tajo.user.dto.response;

import com.energy.tajo.user.domain.User;
import lombok.Getter;

@Getter
public class UserCreateResponse {

    private final Long id;

    private final String name;

    private final String tell;

    private final String email;

    private UserCreateResponse(final Long id, final String name, final String tell, final String email) {
        this.id = id;
        this.name = name;
        this.tell = tell;
        this.email = email;
    }

    public static UserCreateResponse from(final User user) {
        return new UserCreateResponse(user.getId(), user.getName(), user.getTell(), user.getEmail());
    }
}