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
      throw translateLoginStateError(e);
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

  private RuntimeException translateLoginStateError(PkoStateError pkoStateError) {
    return switch (pkoStateError.getStateId()) {
      case "login" -> new InvalidLogin("Invalid login was provided");
      case "captcha" -> new CaptchaNeeded("Please go to https://www.ipko.pl/ and resolve CAPTCHA check");
      default -> new RuntimeException(pkoStateError.getCause());
    };
  }

  private Response fetchPasswordRequest(String password, SessionAttributes sessionAttributes) {
    try {
      JsonPostRequest passwordRequest = Requests.createPasswordRequest(password, sessionAttributes);
      return pkoClient.fetch(passwordRequest);
    } catch (PkoStateError e) {
      throw translateSignInPasswordStateError(e);
    }
  }

  private RuntimeException translateSignInPasswordStateError(PkoStateError pkoStateError) {
    return switch (pkoStateError.getStateId()) {
      case "login" -> new InvalidPassword("Invalid password was provided");
      case "blocked_channel" -> new BlockedChannel("Please go to https://www.ipko.pl/ and unblock your account");
      case "ERROR" ->
          new UnsuccessfulSignIn("Encountered an error during password request fetch: " + pkoStateError.getMessage());
      default -> new RuntimeException(pkoStateError.getCause());
    };
  }

  private void assertSignedIn(Response passwordResponse) {
    String stateId = passwordResponse.toJson().get("state_id").textValue();
    boolean finished = passwordResponse.toJson().get("finished").asBoolean();
    if (!stateId.equals("END") || !finished) {
      throw new UnsuccessfulSignIn("Sign In response does not contain fields confirming successful signing in");
    }
  }
}
