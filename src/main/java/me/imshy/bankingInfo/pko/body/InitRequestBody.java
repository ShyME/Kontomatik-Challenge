package me.imshy.bankingInfo.pko.body;

import java.util.Map;

public class InitRequestBody {
  public final Map<String, Object> data = Map.of(
      "account_ids", Map.of(),
      "accounts", Map.of()
  );
}
