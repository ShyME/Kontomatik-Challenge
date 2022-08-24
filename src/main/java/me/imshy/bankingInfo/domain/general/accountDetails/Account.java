package me.imshy.bankingInfo.domain.general.accountDetails;

import java.math.BigDecimal;

public record Account(String accountNumber, String currency, BigDecimal balance) {

}
