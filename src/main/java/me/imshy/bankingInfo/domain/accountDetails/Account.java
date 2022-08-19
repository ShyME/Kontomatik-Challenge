package me.imshy.bankingInfo.domain.accountDetails;

import java.math.BigDecimal;

public record Account(String accountId, String currency, BigDecimal balance) {

}
