package me.imshy.bankingInfo.pko.body;

import java.util.Map;

public class PasswordRequestBody {
  public final Map<String, Object> data;
  public final String state_id = "password";
  public final String action = "submit";

  public final String flow_id;
  public final String token;

  public PasswordRequestBody(String password, String flow_id, String token) {
    data = Map.of("password", password);
    this.token = token;
    this.flow_id = flow_id;
  }
}
