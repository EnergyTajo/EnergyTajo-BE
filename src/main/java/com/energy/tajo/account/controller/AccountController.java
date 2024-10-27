package com.energy.tajo.account.controller;

import com.energy.tajo.account.dto.request.AccountRequest;
import com.energy.tajo.account.dto.response.AccountResponse;
import com.energy.tajo.account.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    // 계좌 등록
    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserUuid = (String) authentication.getPrincipal();
        AccountResponse response = accountService.createAccount(request, authenticatedUserUuid);
        return ResponseEntity.ok(response);
    }

    // 계좌 조회 (유저별 계좌 리스트)
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccountsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserUuid = (String) authentication.getPrincipal();
        List<AccountResponse> accounts = accountService.getAccountsByUser(authenticatedUserUuid);
        return ResponseEntity.ok(accounts);
    }

    // 포인트 -> 동백전
    @PostMapping("/point_to_cash")
    public ResponseEntity<String> deposit(@RequestBody AccountRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserUuid = (String) authentication.getPrincipal();
        accountService.deposit(request, authenticatedUserUuid);
        return ResponseEntity.ok("입금이 완료되었습니다.");
    }
}