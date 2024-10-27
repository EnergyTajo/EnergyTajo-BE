package com.energy.tajo.account.service;

import com.energy.tajo.account.domain.Account;
import com.energy.tajo.account.dto.request.AccountRequest;
import com.energy.tajo.account.dto.response.AccountResponse;
import com.energy.tajo.account.repository.AccountRepository;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.points.domain.Charge;
import com.energy.tajo.points.repository.ChargeRepository;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ChargeRepository chargeRepository;

    // 계좌 생성
    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        User user = userRepository.findByUuid(request.userId())
            .orElseThrow(() -> new EnergyException(ErrorCode.USER_NOT_FOUND));

        boolean accountExists = accountRepository.findByAccountNumAndBankName(request.accountNum(), request.bankName()).isPresent();
        if (accountExists) {
            throw new EnergyException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }

        Account account = Account.of(request.accountNum(), user, request.bankName(), 0);
        accountRepository.save(account);

        return AccountResponse.of(account);
    }

    // 유저의 계좌 리스트 조회
    public List<AccountResponse> getAccountsByUser(String userId) {
        User user = userRepository.findByUuid(userId)
            .orElseThrow(() -> new EnergyException(ErrorCode.USER_NOT_FOUND));

        return accountRepository.findByUser(user).stream()
            .map(AccountResponse::of)
            .toList();
    }

    // 포인트 전환 (포인트 -> 동백전 입금)
    @Transactional
    public void deposit(AccountRequest request) {
        User user = userRepository.findByUuid(request.userId())
            .orElseThrow(() -> new EnergyException(ErrorCode.USER_NOT_FOUND));

        if (user.getPoints() < request.amount()) {
            throw new EnergyException(ErrorCode.INSUFFICIENT_POINTS);
        }

        Account account = accountRepository.findByAccountNum(request.accountNum())
            .orElseThrow(() -> new EnergyException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.setPoints(user.getPoints() - request.amount());
        account.setBalance(account.getBalance() + request.amount());

        String lastFourDigits = account.getAccountNum().length() > 4 ?
            account.getAccountNum().substring(account.getAccountNum().length() - 4) : account.getAccountNum();

        Charge pointSpentTransaction = Charge.of(user, request.amount(), user.getPoints(), lastFourDigits);
        chargeRepository.save(pointSpentTransaction);

        accountRepository.save(account);
        userRepository.save(user);
    }
}