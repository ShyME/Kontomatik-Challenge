package me.imshy.bankingInfo.pko;

import me.imshy.bankingInfo.general.accountDetails.LoginCredentials;
import me.imshy.bankingInfo.general.exception.UnsuccessfulSignIn;
import me.imshy.bankingInfo.general.http.client.HttpClient;
import me.imshy.bankingInfo.general.http.client.IHttpClient;
import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.RequestResponse;
import me.imshy.bankingInfo.pko.util.RequestCreator;
import me.imshy.bankingInfo.pko.util.ResponseParserUtils;

public class PkoSignIn {

  private final String LOGIN_URL = "https://www.ipko.pl/ipko3/login";

  private final IHttpClient httpClient;

  private SessionAttributes sessionAttributes;

  public PkoSignIn() {
    this.httpClient = new HttpClient();
  }

  public PkoConnection login(LoginCredentials loginCredentials) {
    executeLoginRequest(loginCredentials.login());
    executePasswordRequest(loginCredentials.password());
    return new PkoConnection(httpClient, sessionAttributes);
  }

  private RequestResponse executeLoginRequest(String login) {
    PostRequest loginRequest = RequestCreator.getLoginRequest(LOGIN_URL, login);
    RequestResponse loginResponse = httpClient.sendRequest(loginRequest);
    if (!loginResponse.isSuccessful()) {
      throw new UnsuccessfulSignIn("Bad login");
    }

    sessionAttributes = ResponseParserUtils.parseSessionAttributes(loginResponse);

    return loginResponse;
  }

  private RequestResponse executePasswordRequest(String password) throws UnsuccessfulSignIn {
    PostRequest passwordRequest = RequestCreator.getPasswordRequest(LOGIN_URL, password, sessionAttributes);
    RequestResponse passwordResponse = httpClient.sendRequest(passwordRequest);
    if (!passwordResponse.isSuccessful()) {
      throw new UnsuccessfulSignIn("Bad password");
    }

    return passwordResponse;
  }


}
