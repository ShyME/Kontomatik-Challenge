package me.imshy.accountDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
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
