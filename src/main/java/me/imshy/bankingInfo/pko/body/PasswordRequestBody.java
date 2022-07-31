package me.imshy.bankingInfo.pko.body;

import me.imshy.bankingInfo.pko.SessionAttributes;

import java.util.Map;

public class PasswordRequestBody {
  public final Map<String, Object> data;
  public final String state_id = "password";
  public final String action = "submit";

  public final String flow_id;
  public final String token;

  public PasswordRequestBody(String password, SessionAttributes sessionAttributes) {
    data = Map.of("password", password);
    this.token = sessionAttributes.token();
    this.flow_id = sessionAttributes.flowId();
  }
}
