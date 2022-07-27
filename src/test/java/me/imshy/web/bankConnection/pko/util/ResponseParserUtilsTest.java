package me.imshy.web.bankConnection.pko.util;

import static org.assertj.core.api.Assertions.assertThat;

import me.imshy.accountDetails.AccountBalance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class ResponseParserUtilsTest {

    @Autowired
    private ResponseParserUtils responseParserUtils;

    private String initResponseJson = """
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
        List<AccountBalance> accountBalanceList = responseParserUtils.parseAccountBalances(initResponseJson);
        assertThat(accountBalanceList.size()).isEqualTo(1);
        assertThat(accountBalanceList.get(0).getBalance()).isEqualTo(new BigDecimal("200.00"));
    }
}