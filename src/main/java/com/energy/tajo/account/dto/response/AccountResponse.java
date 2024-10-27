package com.energy.tajo.account.dto.response;

import com.energy.tajo.account.domain.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse {
    private String accountNum;
    private String bankName;
    private int balance;

    public static AccountResponse of(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNum(account.getAccountNum());
        response.setBankName(account.getBankName());
        response.setBalance(account.getBalance());
        return response;
    }
}