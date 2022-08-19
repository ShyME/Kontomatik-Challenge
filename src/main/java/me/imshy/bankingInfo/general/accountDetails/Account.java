package me.imshy.bankingInfo.general.accountDetails;

import java.math.BigDecimal;

public record Account(String accountId, String currency, BigDecimal balance) {

}
