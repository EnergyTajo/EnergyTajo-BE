package com.energy.tajo.user.controller;

import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.dto.response.UserDataResponse;
import com.energy.tajo.user.service.UserService;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userData")
    public ResponseEntity<UserDataResponse> getUserData(Principal principal) {
        String uuid = principal.getName();
        User user = userService.findUserById(uuid);

        UserDataResponse response = new UserDataResponse(user.getPoints(), user.getTotPowerGen());
        return ResponseEntity.ok(response);
    }
}