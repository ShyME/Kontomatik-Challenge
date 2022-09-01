package me.imshy.scraper.domain.pko;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.scraper.domain.Credentials;
import me.imshy.scraper.domain.exception.AccessBlocked;
import me.imshy.scraper.domain.exception.InvalidCredentials;
import me.imshy.scraper.domain.http.HttpClient;
import me.imshy.scraper.domain.http.JsonPostRequest;
import me.imshy.scraper.domain.http.Response;
import me.imshy.scraper.domain.pko.http.PkoRequests;

public class PkoSignIn {

  private final HttpClient httpClient;

  public PkoSignIn(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public PkoSession signIn(Credentials credentials) {
    SessionAttributes sessionAttributes = enterLogin(credentials.login());
    enterPassword(credentials.password(), sessionAttributes);
    return new PkoSession(httpClient, sessionAttributes.sessionId());
  }

  private SessionAttributes enterLogin(String login) {
    JsonPostRequest loginRequest = PkoRequests.createLoginRequest(login);
    Response loginResponse = httpClient.fetch(loginRequest);
    assertLoginAccepted(loginResponse);
    return parseSessionAttributes(loginResponse);
  }

  private void assertLoginAccepted(Response loginResponse) {
    String stateId = loginResponse.toJson().get("state_id").textValue();
    if (stateId.equals("captcha")) {
      throw new AccessBlocked("Captcha needed");
    } else if (!stateId.equals("password")) {
      throw new RuntimeException("PKO login request unsuccessful");
    }
  }

  private SessionAttributes parseSessionAttributes(Response loginResponse) {
    String sessionId = loginResponse.headers().get("X-Session-Id");
    JsonNode loginJson = loginResponse.toJson();
    return new SessionAttributes(
        sessionId,
        loginJson.get("flow_id").textValue(),
        loginJson.get("token").textValue()
    );
  }

  private void enterPassword(String password, SessionAttributes sessionAttributes) {
    JsonPostRequest passwordRequest = PkoRequests.createPasswordRequest(password, sessionAttributes);
    Response passwordResponse = httpClient.fetch(passwordRequest);
    assertSignedIn(passwordResponse);
  }

  private void assertSignedIn(Response passwordResponse) {
    String stateId = passwordResponse.toJson().get("state_id").textValue();
    if (stateId.equals("blocked_channel")) {
      throw new AccessBlocked("Blocked account");
    }
    if (!stateId.equals("END")) {
      throw new InvalidCredentials("Invalid credentials");
    }
  }

}
