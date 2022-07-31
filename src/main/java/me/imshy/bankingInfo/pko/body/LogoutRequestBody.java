package me.imshy.bankingInfo.pko.body;

import java.util.Map;

public class LogoutRequestBody {
  public final Map<String, Object> data = Map.of("reason", "CLIENT_LOGOUT");
}
