package me.imshy.bankingInfo.pko.util;

import me.imshy.bankingInfo.general.util.JsonUtils;
import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.pko.SessionAttributes;
import me.imshy.bankingInfo.pko.body.InitRequestBody;
import me.imshy.bankingInfo.pko.body.LoginRequestBody;
import me.imshy.bankingInfo.pko.body.LogoutRequestBody;
import me.imshy.bankingInfo.pko.body.PasswordRequestBody;

public class Requests {

  private final static String LOGIN_URL = "https://www.ipko.pl/ipko3/login";
  private final static String INIT_URL = "https://www.ipko.pl/ipko3/init";
  private final static String LOGOUT_URL = "https://www.ipko.pl/ipko3/logout";
  public static PostRequest getLoginRequest(String login) {
    return new PostRequest.PostRequestBuilder(LOGIN_URL)
        .setBody(JsonUtils.getJsonAsString(new LoginRequestBody(login)))
        .build();
  }

  public static PostRequest getPasswordRequest(String password, SessionAttributes sessionAttributes) {
    return new PostRequest.PostRequestBuilder(LOGIN_URL)
        .setBody(JsonUtils.getJsonAsString(new PasswordRequestBody(password, sessionAttributes.flowId(), sessionAttributes.token())))
        .addHeader("x-session-id", sessionAttributes.sessionId())
        .build();
  }

  public static PostRequest getLogoutRequest(String sessionId) {
    return new PostRequest.PostRequestBuilder(LOGOUT_URL)
        .setBody(JsonUtils.getJsonAsString(new LogoutRequestBody()))
        .addHeader("x-session-id", sessionId)
        .build();
  }

  public static PostRequest getInitRequest(String sessionId) {
    return new PostRequest.PostRequestBuilder(INIT_URL)
        .setBody(JsonUtils.getJsonAsString(new InitRequestBody()))
        .addHeader("x-session-id", sessionId)
        .build();
  }
}
