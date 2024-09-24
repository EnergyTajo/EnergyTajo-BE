package com.energy.tajo.user.service;

import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.dto.request.UserCreateRequest;
import com.energy.tajo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signup(UserCreateRequest userCreateRequest) {

        User user = User.of(userCreateRequest.uuid(), userCreateRequest.pw(), userCreateRequest.name(),
            userCreateRequest.tell(), userCreateRequest.email());

        return userRepository.save(user);
    }
}
