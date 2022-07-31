package me.imshy.bankingInfo.pko.util;

import me.imshy.bankingInfo.general.accountDetails.AccountBalance;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseParserUtilsTest {

  private final String initResponseJson = """
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
    List<AccountBalance> accountBalanceList = ResponseParserUtils.parseAccountBalances(initResponseJson);
    assertThat(accountBalanceList.size()).isEqualTo(1);
    assertThat(accountBalanceList.get(0).balance()).isEqualTo(new BigDecimal("200.00"));
  }
}