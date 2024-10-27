package com.energy.tajo.account.controller;

import com.energy.tajo.account.dto.request.AccountRequest;
import com.energy.tajo.account.dto.response.AccountResponse;
import com.energy.tajo.account.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.ok(response);
    }

    // 계좌 조회 (유저별 계좌 리스트)
    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(@PathVariable String userId) {
        List<AccountResponse> accounts = accountService.getAccountsByUser(userId);
        return ResponseEntity.ok(accounts);
    }

    // 포인트 -> 동백전
    @PostMapping("/point_to_cash")
    public ResponseEntity<String> deposit(@RequestBody AccountRequest request) {
        accountService.deposit(request);
        return ResponseEntity.ok("입금이 완료되었습니다.");
    }

}
