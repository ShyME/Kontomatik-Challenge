package me.imshy.bankingInfo.domain.pko.http.util;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.domain.general.http.request.JsonPostRequest;
import me.imshy.bankingInfo.domain.general.http.request.Response;
import me.imshy.bankingInfo.domain.pko.http.SessionAttributes;
import me.imshy.bankingInfo.domain.pko.http.body.InitRequestBody;
import me.imshy.bankingInfo.domain.pko.http.body.LoginRequestBody;
import me.imshy.bankingInfo.domain.pko.http.body.PasswordRequestBody;
import me.imshy.bankingInfo.infrastructure.general.util.JsonUtils;

import java.util.List;

public class Requests {
  private static final String PKO_BASE_URL = "https://www.ipko.pl/ipko3";
  private static final String LOGIN_PATH = "/login";
  private static final String INIT_PATH = "/init";
  private static final String SESSION_ID_HEADER_NAME = "x-session-id";

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

  public static List<String> getErrorDescriptions(Response response) {
    List<JsonNode> errors = JsonUtils.findValuesByKey("errors", response.body());
    return errors.stream().map(errorNode -> {
      if (!errorNode.get("description").textValue().equals(""))
        return errorNode.get("description").textValue();
      else
        return errorNode.get("hint").textValue();
    }).toList();
  }
}
