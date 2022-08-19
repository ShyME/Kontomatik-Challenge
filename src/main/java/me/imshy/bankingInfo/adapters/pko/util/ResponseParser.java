package me.imshy.bankingInfo.adapters.pko.util;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.adapters.general.http.request.Response;
import me.imshy.bankingInfo.domain.accountDetails.Account;
import me.imshy.bankingInfo.domain.exception.UnsuccessfulSignIn;
import me.imshy.bankingInfo.adapters.general.util.JsonUtils;
import me.imshy.bankingInfo.adapters.pko.SessionAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResponseParser {
  public static SessionAttributes parseSessionAttributes(Response loginResponse) {
    String sessionId = loginResponse.headers().get("X-Session-Id");
    if (sessionId == null || sessionId.isEmpty()) {
      throw new UnsuccessfulSignIn("Response lacks session id");
    }

    JsonNode loginJson = JsonUtils.getJsonAsNode(loginResponse.body());

    String flowId = loginJson.get("flow_id").textValue();
    String token = loginJson.get("token").textValue();

    return new SessionAttributes(
        sessionId,
        flowId,
        token
    );
  }

  public static List<Account> parseAccountBalances(String initResponseJson) {
    List<Account> accountBalances = new ArrayList<>(1);

    JsonNode initJson = JsonUtils.getJsonAsNode(initResponseJson).get("response").get("data");

    List<String> accountIds = JsonUtils.parseFlatStringArray(initJson.get("account_ids").toString());
    JsonNode accountsJson = initJson.get("accounts");

    for (String accountId : accountIds) {
      JsonNode accountJson = accountsJson.get(accountId);
      String currency = accountJson.get("currency").textValue();
      String balance = accountJson.get("ledger").textValue();

      accountBalances.add(new Account(
          accountId,
          currency,
          new BigDecimal(balance)
      ));
    }

    return accountBalances;
  }
}
