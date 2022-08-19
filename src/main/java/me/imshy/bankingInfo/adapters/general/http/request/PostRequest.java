package me.imshy.bankingInfo.adapters.general.http.request;

import java.util.HashMap;
import java.util.Map;

public record PostRequest(String url, Map<String, String> headers, String body) {
  public static class PostRequestBuilder {
    private final String url;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public PostRequestBuilder(String url) {
      this.url = url;
      addDefaultJsonContentHeaders();
    }

    public PostRequestBuilder addHeader(String name, String value) {
      this.headers.put(name, value);
      return this;
    }

    public PostRequestBuilder setBody(String body) {
      this.body = body;
      return this;
    }

    public PostRequest build() {
      return new PostRequest(this.url, this.headers, this.body);
    }

    private void addDefaultJsonContentHeaders() {
      headers.put("Accept", "application/json");
      headers.put("Content-type", "application/json");
    }
  }
}
