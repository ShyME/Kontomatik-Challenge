package me.imshy.bankingInfo.domain.pko.http;

import me.imshy.bankingInfo.adapters.general.exception.RequestError;
import me.imshy.bankingInfo.adapters.general.http.client.HttpClient;
import me.imshy.bankingInfo.adapters.general.http.request.PostRequest;
import me.imshy.bankingInfo.adapters.general.http.request.Response;
import me.imshy.bankingInfo.adapters.pko.util.Requests;
import me.imshy.bankingInfo.adapters.pko.util.ResponseParser;
import me.imshy.bankingInfo.domain.accountDetails.Account;

import java.util.List;

public class PkoConnection {

  private final HttpClient httpClient;
  private final String sessionId;

  PkoConnection(HttpClient httpClient, String sessionId) {
    this.httpClient = httpClient;
    this.sessionId = sessionId;
  }

  public List<Account> getAccountBalances() throws RequestError {
    Response initResponse = executeInitRequest();

    return ResponseParser.parseAccountBalances(initResponse.body());
  }

  public void logout() {
    PostRequest logoutRequest = Requests.createLogoutRequest(sessionId);
    httpClient.fetchRequest(logoutRequest);
  }

  private Response executeInitRequest() {
    PostRequest initRequest = Requests.createInitRequest(sessionId);

    return httpClient.fetchRequest(initRequest);
  }
}
