package me.imshy.bankingInfo.domain.pko.http.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LoginRequestBody {
  public final Map<String, Object> data;
  @JsonProperty("state_id")
  public final String stateId = "login";
  public final String action = "submit";

  public LoginRequestBody(String login) {
    data = Map.of("login", login);
  }
}
