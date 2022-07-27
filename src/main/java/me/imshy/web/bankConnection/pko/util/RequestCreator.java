package me.imshy.web.bankConnection.pko.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.util.JsonUtils;
import me.imshy.web.bankConnection.pko.SessionAttributes;
import me.imshy.web.bankConnection.pko.request.body.InitRequestBody;
import me.imshy.web.bankConnection.pko.request.body.LoginRequestBody;
import me.imshy.web.bankConnection.pko.request.body.LogoutRequestBody;
import me.imshy.web.bankConnection.pko.request.body.PasswordRequestBody;
import me.imshy.web.request.PostRequest;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class RequestCreator {

    public static PostRequest getLoginRequest(String loginUrl, String login) throws JsonProcessingException {
        return new PostRequest.PostRequestBuilder(loginUrl)
                .requestBody(new StringEntity(JsonUtils.getJson(new LoginRequestBody(login))))
                .build();
    }

    public static PostRequest getPasswordRequest(String loginUrl, String password, SessionAttributes sessionAttributes) throws JsonProcessingException {
        return new PostRequest.PostRequestBuilder(loginUrl)
                .requestBody(new StringEntity(JsonUtils.getJson(new PasswordRequestBody(password, sessionAttributes))))
                .addHeader("x-session-id", sessionAttributes.getSessionId())
                .build();
    }

    public static PostRequest getLogoutRequest(String logoutUrl, String sessionId) throws JsonProcessingException {
        return new PostRequest.PostRequestBuilder(logoutUrl)
                .requestBody(new StringEntity(JsonUtils.getJson(new LogoutRequestBody())))
                .addHeader("x-session-id", sessionId)
                .build();
    }

    public static PostRequest getInitRequest(String initUrl, String sessionId) throws JsonProcessingException {
        return new PostRequest.PostRequestBuilder(initUrl)
                .requestBody(new StringEntity(JsonUtils.getJson(new InitRequestBody())))
                .addHeader("x-session-id", sessionId)
                .build();
    }
}
