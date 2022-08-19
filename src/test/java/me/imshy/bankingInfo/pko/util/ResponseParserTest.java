package me.imshy.bankingInfo.pko.util;

import me.imshy.bankingInfo.adapters.pko.util.ResponseParser;
import me.imshy.bankingInfo.domain.accountDetails.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseParserTest {

  private final String INIT_RESPONSE_JSON = """
      {
      	"httpStatus": 200,
      	"response": {
      		"data": {
      			"accounts": {
      				"accountIdValue": {
      					"ledger": "200.00",
      					"currency": "PLN"
      				}
      			},
      			"account_ids": [
      				"accountIdValue"
      			]
      		}
      	}
      }
      """;

  @Test
  void parseAccountBalances_shouldSuccess() {
    List<Account> accountList = ResponseParser.parseAccountBalances(INIT_RESPONSE_JSON);
    assertThat(accountList.size()).isEqualTo(1);
    assertThat(accountList.get(0).balance()).isEqualTo(new BigDecimal("200.00"));
  }
}