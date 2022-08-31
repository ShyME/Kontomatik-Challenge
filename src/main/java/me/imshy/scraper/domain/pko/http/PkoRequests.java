package me.imshy.scraper.domain.pko.http;

import me.imshy.scraper.domain.http.request.JsonPostRequest;
import me.imshy.scraper.domain.pko.SessionAttributes;

public class PkoRequests {

  public static final String PKO_BASE_URL = "https://www.ipko.pl";
  public static final String LOGIN_PATH = "/ipko3/login";
  public static final String INIT_PATH = "/ipko3/init";
  public static final String SESSION_ID_HEADER_NAME = "X-Session-Id";

  public static JsonPostRequest createLoginRequest(String login) {
    return new JsonPostRequest.Builder(PKO_BASE_URL + LOGIN_PATH)
        .setBody(new LoginRequestBody(login))
        .build();
  }

  public static JsonPostRequest createPasswordRequest(String password, SessionAttributes sessionAttributes) {
    return new JsonPostRequest.Builder(PKO_BASE_URL + LOGIN_PATH)
        .setBody(new PasswordRequestBody(password, sessionAttributes.flowId(), sessionAttributes.token()))
        .addHeader(SESSION_ID_HEADER_NAME, sessionAttributes.sessionId())
        .build();
  }

  public static JsonPostRequest createInitRequest(String sessionId) {
    return new JsonPostRequest.Builder(PKO_BASE_URL + INIT_PATH)
        .setBody(new InitRequestBody())
        .addHeader(SESSION_ID_HEADER_NAME, sessionId)
        .build();
  }

}
