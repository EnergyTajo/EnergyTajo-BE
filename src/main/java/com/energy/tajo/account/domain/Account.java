package com.energy.tajo.account.domain;

import com.energy.tajo.global.domain.BaseTimeEntity;
import com.energy.tajo.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Account extends BaseTimeEntity {

    @Id
    @Column(nullable = false, name="account_num")
    private String accountNum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    private User user;

    @Column(nullable = false, name="bank_name")
    private String bankName;

    @Column(nullable = false)
    private int balance;

    private Account(String accountNum, User user, String bankName, int balance) {
        this.accountNum = accountNum;
        this.user = user;
        this.bankName = bankName;
        this.balance = balance;
    }

    public static Account of(String accountNum, User user, String bankName, int balance) {
        return new Account(accountNum, user, bankName, balance);
    }
}
