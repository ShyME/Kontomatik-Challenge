package me.imshy.bankingInfo.domain.pko.http;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.adapters.general.exception.HttpCodeError;
import me.imshy.bankingInfo.domain.general.accountDetails.Account;
import me.imshy.bankingInfo.domain.general.http.request.JsonPostRequest;
import me.imshy.bankingInfo.domain.general.http.request.Response;
import me.imshy.bankingInfo.domain.pko.http.util.Requests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PkoSession {
  private final PkoClient pkoClient;
  private final String sessionId;

  PkoSession(PkoClient pkoClient, String sessionId) {
    this.pkoClient = pkoClient;
    this.sessionId = sessionId;
  }

  public List<Account> importAccounts() throws HttpCodeError {
    Response initResponse = executeInitRequest();
    return parseAccounts(initResponse);
  }

  private Response executeInitRequest() {
    JsonPostRequest initRequest = Requests.createInitRequest(sessionId);
    return pkoClient.fetch(initRequest);
  }

  private static List<Account> parseAccounts(Response initResponse) {
    JsonNode accountsJson = initResponse.toJson().get("response").get("data").get("accounts");
    List<Account> accounts = new ArrayList<>();
    for (Iterator<JsonNode> it = accountsJson.elements(); it.hasNext(); ) {
      accounts.add(deserializeAccountJson(it.next()));
    }
    return accounts;
  }

  private static Account deserializeAccountJson(JsonNode accountJson) {
    String accountNumber = accountJson.get("number").get("value").textValue();
    String currency = accountJson.get("currency").textValue();
    BigDecimal balance = new BigDecimal(accountJson.get("balance").textValue());
    return new Account(accountNumber, currency, balance);
  }
}
