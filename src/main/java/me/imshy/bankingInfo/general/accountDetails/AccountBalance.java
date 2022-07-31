package me.imshy.bankingInfo.general.accountDetails;

import java.math.BigDecimal;

public record AccountBalance(String accountId, String currency, BigDecimal balance) {

  public String toString() {
    return "Account ID = "
        + accountId +
        " : "
        + balance +
        " " +
        currency;
  }
}
