package me.imshy.bankingInfo.domain.general.http.request;

import me.imshy.bankingInfo.adapters.general.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public record JsonPostRequest(String url, Map<String, String> headers, String body) {


  public static class Builder {
    private final String url;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public Builder(String url) {
      this.url = url;
    }

    public Builder addHeader(String name, String value) {
      this.headers.put(name, value);
      return this;
    }

    public Builder setBody(Object body) {
      this.body = JsonUtils.getJsonAsString(body);
      return this;
    }

    public JsonPostRequest build() {
      headers.putAll(Map.of("Accept", "application/json", "Content-type", "application/json"));
      return new JsonPostRequest(this.url, this.headers, this.body);
    }
  }
}
