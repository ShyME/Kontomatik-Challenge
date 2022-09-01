package me.imshy.scraper.domain.pko;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.scraper.domain.Account;
import me.imshy.scraper.domain.http.HttpClient;
import me.imshy.scraper.domain.http.JsonPostRequest;
import me.imshy.scraper.domain.http.Response;
import me.imshy.scraper.domain.pko.http.PkoRequests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PkoSession {

  private final HttpClient httpClient;
  private final String sessionId;

  PkoSession(HttpClient httpClient, String sessionId) {
    this.httpClient = httpClient;
    this.sessionId = sessionId;
  }

  public List<Account> importAccounts() {
    Response initResponse = executeInitRequest();
    return parseAccounts(initResponse);
  }

  private Response executeInitRequest() {
    JsonPostRequest initRequest = PkoRequests.createInitRequest(sessionId);
    return httpClient.fetch(initRequest);
  }

  private static List<Account> parseAccounts(Response initResponse) {
    JsonNode accountsJson = initResponse.toJson().get("response").get("data").get("accounts");
    List<Account> accounts = new ArrayList<>();
    accountsJson.elements().forEachRemaining(accountNode ->
        accounts.add(deserializeAccountJson(accountNode))
    );
    return accounts;
  }

  private static Account deserializeAccountJson(JsonNode accountJson) {
    String accountNumber = accountJson.get("number").get("value").textValue();
    String currency = accountJson.get("currency").textValue();
    BigDecimal balance = new BigDecimal(accountJson.get("balance").textValue());
    return new Account(accountNumber, currency, balance);
  }

}
