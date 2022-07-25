package me.imshy.account;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class AccountBalance {
    private String accountId;
    private String currency;
    private BigDecimal balance;

    public String toString() {
        return "Account ID = "
                + accountId +
                " : "
                + balance +
                " " +
                currency;
    }
}
