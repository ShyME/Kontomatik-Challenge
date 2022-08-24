package me.imshy.bankingInfo.domain.pko.http.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PasswordRequestBody {
  public final Map<String, Object> data;
  @JsonProperty("state_id")
  public final String stateId = "password";
  public final String action = "submit";
  @JsonProperty("flow_id")
  public final String flowId;
  public final String token;

  public PasswordRequestBody(String password, String flowId, String token) {
    data = Map.of("password", password);
    this.token = token;
    this.flowId = flowId;
  }
}
