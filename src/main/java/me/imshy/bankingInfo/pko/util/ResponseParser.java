package me.imshy.bankingInfo.pko.util;

import me.imshy.bankingInfo.general.accountDetails.Account;
import me.imshy.bankingInfo.general.exception.UnsuccessfulSignIn;
import me.imshy.bankingInfo.general.http.request.Response;
import me.imshy.bankingInfo.general.util.JsonUtils;
import me.imshy.bankingInfo.pko.SessionAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResponseParser {
  public static SessionAttributes parseSessionAttributes(Response loginResponse) {
    String sessionId = loginResponse.headers().get("X-Session-Id");
    if (sessionId == null || sessionId.isEmpty()) {
      throw new UnsuccessfulSignIn("Response lacks session id");
    }

    String flowId = JsonUtils.getValueFromJson("flow_id", loginResponse.body()).replace("\"", "");
    String token = JsonUtils.getValueFromJson("token", loginResponse.body()).replace("\"", "");

    return new SessionAttributes(
        sessionId,
        flowId,
        token
    );
  }

  public static List<Account> parseAccountBalances(String initResponseJson) {
    List<Account> accountBalances = new ArrayList<>(1);

    String accountIds = JsonUtils.getValueFromJson("account_ids", initResponseJson);

    List<String> accounts = JsonUtils.parseFlatStringArray(accountIds);
    String accountsJson = JsonUtils.getValueFromJson("accounts", initResponseJson);

    for (String accountId : accounts) {
      String currency = JsonUtils.getValueFromJson("currency", accountsJson).replace("\"", "");
      String balance = JsonUtils.getValueFromJson("ledger", accountsJson).replace("\"", "");

      Account account = new Account(
          accountId,
          currency,
          new BigDecimal(balance)
      );

      accountBalances.add(account);
    }

    return accountBalances;
  }
}
