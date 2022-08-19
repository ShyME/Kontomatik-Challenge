package me.imshy.bankingInfo.pko;

import me.imshy.bankingInfo.general.accountDetails.Account;
import me.imshy.bankingInfo.general.exception.RequestError;
import me.imshy.bankingInfo.general.http.client.HttpClient;
import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.Response;
import me.imshy.bankingInfo.pko.util.Requests;
import me.imshy.bankingInfo.pko.util.ResponseParser;

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
    PostRequest logoutRequest = Requests.getLogoutRequest(sessionId);
    httpClient.fetchRequest(logoutRequest);
  }

  private Response executeInitRequest() {
    PostRequest initRequest = Requests.getInitRequest(sessionId);

    return httpClient.fetchRequest(initRequest);
  }
}
