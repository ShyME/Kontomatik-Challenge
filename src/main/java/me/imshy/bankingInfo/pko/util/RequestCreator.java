package me.imshy.bankingInfo.pko.util;

import me.imshy.bankingInfo.general.util.JsonUtils;
import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.pko.SessionAttributes;
import me.imshy.bankingInfo.pko.body.InitRequestBody;
import me.imshy.bankingInfo.pko.body.LoginRequestBody;
import me.imshy.bankingInfo.pko.body.LogoutRequestBody;
import me.imshy.bankingInfo.pko.body.PasswordRequestBody;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class RequestCreator {

  public static PostRequest getLoginRequest(String loginUrl, String login) {
    return new PostRequest.PostRequestBuilder(loginUrl)
        .requestBody(new StringEntity(JsonUtils.getJson(new LoginRequestBody(login))))
        .build();
  }

  public static PostRequest getPasswordRequest(String loginUrl, String password, SessionAttributes sessionAttributes) {
    return new PostRequest.PostRequestBuilder(loginUrl)
        .requestBody(new StringEntity(JsonUtils.getJson(new PasswordRequestBody(password, sessionAttributes))))
        .addHeader("x-session-id", sessionAttributes.sessionId())
        .build();
  }

  public static PostRequest getLogoutRequest(String logoutUrl, String sessionId) {
    return new PostRequest.PostRequestBuilder(logoutUrl)
        .requestBody(new StringEntity(JsonUtils.getJson(new LogoutRequestBody())))
        .addHeader("x-session-id", sessionId)
        .build();
  }

  public static PostRequest getInitRequest(String initUrl, String sessionId) {
    return new PostRequest.PostRequestBuilder(initUrl)
        .requestBody(new StringEntity(JsonUtils.getJson(new InitRequestBody())))
        .addHeader("x-session-id", sessionId)
        .build();
  }
}
