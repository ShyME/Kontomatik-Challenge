package me.imshy.bankingInfo.adapters.pko.util;

import me.imshy.bankingInfo.adapters.general.http.request.Response;
import me.imshy.bankingInfo.adapters.pko.SessionAttributes;
import me.imshy.bankingInfo.domain.accountDetails.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

  private final String loginResponseBody = """
      {
      	"httpStatus": 200,
      	"state_id": "password",
      	"token": "abcdTOKEN",
      	"flow_id": "sampleFlowId",
      	"response": {
      		"fields": null,
      		"data": null
      	}
      }""";

  private final String loginResponseBodyNoFlowId = """
      {
      	"httpStatus": 200,
      	"state_id": "password",
      	"token": "abcdTOKEN",
      	"response": {
      		"fields": null,
      		"data": null
      	}
      }""";

  private final Response loginResponse = new Response.ResponseBuilder()
      .setBody(loginResponseBody)
      .setHeaders(Map.of("X-Session-Id", "sessionIdValue"))
      .setCode(200)
      .build();

  private final Response loginResponseNoFlowId = new Response.ResponseBuilder()
      .setBody(loginResponseBodyNoFlowId)
      .setHeaders(Map.of("X-Session-Id", "sessionIdValue"))
      .setCode(200)
      .build();

  @Test
  void parseAccountBalances_shouldSuccess() {
    List<Account> accountList = ResponseParser.parseAccountBalances(INIT_RESPONSE_JSON);
    assertThat(accountList.size()).isEqualTo(1);
    assertThat(accountList.get(0).balance()).isEqualTo(new BigDecimal("200.00"));
  }

  @Test
  void parseSessionAttributes_shouldSuccess() {
    SessionAttributes sessionAttributes = ResponseParser.parseSessionAttributes(loginResponse);
    assertThat(sessionAttributes.sessionId()).isEqualTo("sessionIdValue");
    assertThat(sessionAttributes.token()).isEqualTo("abcdTOKEN");
    assertThat(sessionAttributes.flowId()).isEqualTo("sampleFlowId");
  }

  @Test
  void noFlowId_parseSessionAttributes_shouldThrowNullPointer() {
    assertThatThrownBy(() -> {
      ResponseParser.parseSessionAttributes(loginResponseNoFlowId);
    }).isInstanceOf(NullPointerException.class);
  }
}