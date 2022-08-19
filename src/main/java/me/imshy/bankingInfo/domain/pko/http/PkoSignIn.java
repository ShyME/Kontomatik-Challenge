package me.imshy.bankingInfo.domain.pko.http;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.adapters.pko.SessionAttributes;
import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import me.imshy.bankingInfo.adapters.general.exception.RequestError;
import me.imshy.bankingInfo.domain.exception.UnsuccessfulSignIn;
import me.imshy.bankingInfo.adapters.general.http.client.apache.ApacheHttpClient;
import me.imshy.bankingInfo.adapters.general.http.client.HttpClient;
import me.imshy.bankingInfo.adapters.general.http.request.PostRequest;
import me.imshy.bankingInfo.adapters.general.http.request.Response;
import me.imshy.bankingInfo.adapters.pko.util.Requests;
import me.imshy.bankingInfo.adapters.pko.util.ResponseParser;

public class PkoSignIn {

  private final HttpClient httpClient = new ApacheHttpClient();

  public PkoConnection login(Credentials credentials) {
    try {
      SessionAttributes sessionAttributes = fetchLoginRequest(credentials.login());
      Response passwordResponse = fetchPasswordRequest(credentials.password(), sessionAttributes);

      assertSignedIn(passwordResponse);

      return new PkoConnection(httpClient, sessionAttributes.sessionId());
    } catch(RequestError e) {
      e.printStackTrace();
      throw new UnsuccessfulSignIn(e.ERROR_DESCRIPTIONS.toString());
    }
  }

  private void assertSignedIn(Response passwordResponse) {
    JsonNode passwordResponseJson = passwordResponse.toJson();

    String stateId = passwordResponseJson.get("state_id").textValue();
    boolean finished = passwordResponseJson.get("finished").asBoolean();

    if(!stateId.equals("END") && !finished) {
      throw new UnsuccessfulSignIn("Password response does not contain fields confirming successful Sign In.");
    }
  }

  private SessionAttributes fetchLoginRequest(String login) {
    PostRequest loginRequest = Requests.createLoginRequest(login);
    Response loginResponse = httpClient.fetchRequest(loginRequest);

    String stateId = loginResponse.toJson().get("state_id").textValue();
    if(stateId.equals("captcha")) {
      throw new UnsuccessfulSignIn("Sign In is blocked due to too many unsuccessful attempts.");
    }

    return ResponseParser.parseSessionAttributes(loginResponse);
  }

  private Response fetchPasswordRequest(String password, SessionAttributes sessionAttributes) {
    PostRequest passwordRequest = Requests.createPasswordRequest(password, sessionAttributes);
    return httpClient.fetchRequest(passwordRequest);
  }
}
