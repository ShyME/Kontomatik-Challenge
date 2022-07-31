package me.imshy.bankingInfo.pko.body;

import java.util.Map;

public class LoginRequestBody {
  public final Map<String, Object> data;
  public final String state_id = "login";
  public final String action = "submit";

  public LoginRequestBody(String login) {
    data = Map.of("login", login);
  }
}
