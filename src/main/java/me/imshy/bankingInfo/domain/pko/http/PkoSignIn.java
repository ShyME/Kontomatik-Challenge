package me.imshy.bankingInfo.domain.pko.http;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.domain.general.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.general.http.exception.signIn.*;
import me.imshy.bankingInfo.domain.general.http.request.JsonPostRequest;
import me.imshy.bankingInfo.domain.general.http.request.Response;
import me.imshy.bankingInfo.domain.pko.http.exception.PkoStateError;
import me.imshy.bankingInfo.domain.pko.http.util.Requests;
import me.imshy.bankingInfo.infrastructure.general.util.JsonUtils;

public class PkoSignIn {
  private final PkoClient pkoClient;

  public PkoSignIn(PkoClient pkoClient) {
    this.pkoClient = pkoClient;
  }

  public PkoSession signIn(Credentials credentials) {
    SessionAttributes sessionAttributes = fetchLoginRequest(credentials.login());
    Response passwordResponse = fetchPasswordRequest(credentials.password(), sessionAttributes);
    assertSignedIn(passwordResponse);
    return new PkoSession(pkoClient, sessionAttributes.sessionId());
  }

  private SessionAttributes fetchLoginRequest(String login) {
    try {
      JsonPostRequest loginRequest = Requests.createLoginRequest(login);
      Response loginResponse = pkoClient.fetch(loginRequest);
      return parseSessionAttributes(loginResponse);
    } catch (PkoStateError e) {
      throw new InvalidLogin("Invalid login was provided");
    }
  }

  private SessionAttributes parseSessionAttributes(Response loginResponse) {
    String sessionId = loginResponse.headers().get("X-Session-Id");
    JsonNode loginJson = JsonUtils.getJsonAsNode(loginResponse.body());
    String flowId = loginJson.get("flow_id").textValue();
    String token = loginJson.get("token").textValue();
    return new SessionAttributes(
        sessionId,
        flowId,
        token
    );
  }

  private Response fetchPasswordRequest(String password, SessionAttributes sessionAttributes) {
    try {
      JsonPostRequest passwordRequest = Requests.createPasswordRequest(password, sessionAttributes);
      return pkoClient.fetch(passwordRequest);
    } catch (PkoStateError e) {
      throw translateSignInPasswordException(e);
    }
  }

  private RuntimeException translateSignInPasswordException(PkoStateError e) {
    String stateId = e.getStateId();
    switch (stateId) {
      case "blocked_channel" -> {
        return new BlockedChannel(
            "Sign in was blocked due to too many unsuccessful tries, please go to https://www.ipko.pl/ and resolve the issue"
        );
      }
      case "login" -> {
        return new InvalidPassword("Invalid password was provided");
      }
      case "captcha" -> {
        return new CaptchaNeeded("Please go to https://www.ipko.pl/ and resolve CAPTCHA check");
      }
      default -> {
        return e;
      }
    }
  }

  private void assertSignedIn(Response passwordResponse) {
    JsonNode passwordResponseJson = passwordResponse.toJson();
    String stateId = passwordResponseJson.get("state_id").textValue();
    boolean finished = passwordResponseJson.get("finished").asBoolean();
    if (!stateId.equals("END") && !finished) {
      throw new UnsuccessfulSignIn("Password response does not contain fields confirming successful Sign In.");
    }
  }
}
