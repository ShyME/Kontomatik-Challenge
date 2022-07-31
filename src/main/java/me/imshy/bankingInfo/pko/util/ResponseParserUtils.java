package me.imshy.bankingInfo.pko.util;

import me.imshy.bankingInfo.general.accountDetails.AccountBalance;
import me.imshy.bankingInfo.general.exception.UnsuccessfulSignIn;
import me.imshy.bankingInfo.general.util.JsonUtils;
import me.imshy.bankingInfo.general.http.request.RequestResponse;
import me.imshy.bankingInfo.pko.SessionAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseParserUtils {
  public static SessionAttributes parseSessionAttributes(RequestResponse loginResponse) {
    Optional<String> sessionIdOptional = loginResponse.getHeaderValue("X-Session-Id");
    if (sessionIdOptional.isEmpty()) {
      throw new UnsuccessfulSignIn("Response lacks session id");
    }
    String sessionId = sessionIdOptional.get();

    String flowId = JsonUtils.getValueFromJson("flow_id", loginResponse.getResponseJson()).get().replace("\"", "");
    String token = JsonUtils.getValueFromJson("token", loginResponse.getResponseJson()).get().replace("\"", "");

    return new SessionAttributes(
        sessionId,
        flowId,
        token
    );
  }

  public static List<AccountBalance> parseAccountBalances(String initResponseJson) {
    List<AccountBalance> accountBalances = new ArrayList<>(1);

    String accountIds = JsonUtils.getValueFromJson("account_ids", initResponseJson).get();

    List<String> accounts = JsonUtils.parseFlatStringArray(accountIds);
    String accountsJson = JsonUtils.getValueFromJson("accounts", initResponseJson).get();

    for (String accountId : accounts) {
      String currency = JsonUtils.getValueFromJson("currency", accountsJson).get().replace("\"", "");
      String balance = JsonUtils.getValueFromJson("ledger", accountsJson).get().replace("\"", "");

      AccountBalance accountBalance = new AccountBalance(
          accountId,
          currency,
          new BigDecimal(balance)
      );

      accountBalances.add(accountBalance);
    }

    return accountBalances;
  }
}
