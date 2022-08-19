package me.imshy.bankingInfo.pko;

import me.imshy.bankingInfo.general.accountDetails.Credentials;
import me.imshy.bankingInfo.general.exception.RequestError;
import me.imshy.bankingInfo.general.exception.UnsuccessfulSignIn;
import me.imshy.bankingInfo.general.http.client.apache.ApacheHttpClient;
import me.imshy.bankingInfo.general.http.client.HttpClient;
import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.Response;
import me.imshy.bankingInfo.pko.util.Requests;
import me.imshy.bankingInfo.pko.util.ResponseParser;

public class PkoSignIn {

  private final HttpClient httpClient = new ApacheHttpClient();

  public PkoConnection login(Credentials credentials) {
    try {
      SessionAttributes sessionAttributes = fetchLoginRequest(credentials.login());
      Response passwordResponse = fetchPasswordRequest(credentials.password(), sessionAttributes);

      return new PkoConnection(httpClient, sessionAttributes.sessionId());
    } catch(RequestError e) {
      e.printStackTrace();
      throw new UnsuccessfulSignIn(e.ERROR_DESCRIPTIONS.toString());
    }
  }

  private SessionAttributes fetchLoginRequest(String login) {
    PostRequest loginRequest = Requests.getLoginRequest(login);
    Response loginResponse = httpClient.fetchRequest(loginRequest);

    String stateId = loginResponse.toJson().get("state_id").textValue();
    if(stateId.equals("captcha")) {
      throw new UnsuccessfulSignIn("Sign In is blocked due to too many unsuccessful attempts.");
    }

    return ResponseParser.parseSessionAttributes(loginResponse);
  }

  private Response fetchPasswordRequest(String password, SessionAttributes sessionAttributes) {
    PostRequest passwordRequest = Requests.getPasswordRequest(password, sessionAttributes);
    return httpClient.fetchRequest(passwordRequest);
  }
}
