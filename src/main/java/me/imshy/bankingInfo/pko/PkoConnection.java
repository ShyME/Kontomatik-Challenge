package me.imshy.bankingInfo.pko;

import me.imshy.bankingInfo.general.accountDetails.AccountBalance;
import me.imshy.bankingInfo.general.exception.RequestError;
import me.imshy.bankingInfo.general.http.client.IHttpClient;
import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.RequestResponse;
import me.imshy.bankingInfo.pko.util.RequestCreator;
import me.imshy.bankingInfo.pko.util.ResponseParserUtils;

import java.util.List;

public class PkoConnection {

  private final String INIT_URL = "https://www.ipko.pl/ipko3/init";
  private final String LOGOUT_URL = "https://www.ipko.pl/ipko3/logout";

  private final IHttpClient httpClient;
  private final SessionAttributes sessionAttributes;

  PkoConnection(IHttpClient httpClient, SessionAttributes sessionAttributes) {
    this.httpClient = httpClient;
    this.sessionAttributes = sessionAttributes;
  }

  public List<AccountBalance> getAccountBalances() throws RequestError {
    RequestResponse initResponse = executeInitRequest();

    return ResponseParserUtils.parseAccountBalances(initResponse.getResponseJson());
  }

  public void logout() {
    executeLogoutRequest();
  }

  private RequestResponse executeLogoutRequest() {
    PostRequest logoutRequest = RequestCreator.getLogoutRequest(LOGOUT_URL, sessionAttributes.sessionId());
    RequestResponse logoutResponse = httpClient.sendRequest(logoutRequest);

    if (!logoutResponse.isSuccessful()) {
      throw new RequestError("Bad logoutRequest");
    }

    return logoutResponse;
  }

  private RequestResponse executeInitRequest() {
    PostRequest initRequest = RequestCreator.getInitRequest(INIT_URL, sessionAttributes.sessionId());
    RequestResponse initResponse = httpClient.sendRequest(initRequest);

    if (!initResponse.isSuccessful()) {
      throw new RequestError("Bad initRequest");
    }

    return initResponse;
  }
}
