package com.energy.tajo.user.service;

import static com.energy.tajo.global.exception.ErrorCode.ID_IN_USE;

import com.energy.tajo.global.exception.EnergyException;
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

        if (userRepository.existsByUuid(userCreateRequest.uuid())){
            throw new EnergyException(ID_IN_USE);
        }

        User user = User.of(userCreateRequest.uuid(), userCreateRequest.pw(), userCreateRequest.name(),
            userCreateRequest.phoneNum(), userCreateRequest.email(), userCreateRequest.consentStatus());
        return userRepository.save(user);
    }
}
