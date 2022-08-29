package me.imshy.scraper.domain;

import java.math.BigDecimal;

public record Account(String accountNumber, String currency, BigDecimal balance) {

}
