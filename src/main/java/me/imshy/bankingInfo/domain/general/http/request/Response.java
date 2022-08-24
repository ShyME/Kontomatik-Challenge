package me.imshy.bankingInfo.domain.general.http.request;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.adapters.general.util.JsonUtils;

import java.util.Map;

public record Response(Map<String, String> headers, String body, int code) {
  public JsonNode toJson() {
    return JsonUtils.getJsonAsNode(body);
  }


  public static class ResponseBuilder {
    private Map<String, String> headers;
    private String body;
    private int code;

    public ResponseBuilder setHeaders(Map<String, String> headers) {
      this.headers = headers;
      return this;
    }

    public ResponseBuilder setBody(String body) {
      this.body = body;
      return this;
    }

    public ResponseBuilder setCode(int code) {
      this.code = code;
      return this;
    }

    public Response build() {
      return new Response(headers, body, code);
    }
  }
}
