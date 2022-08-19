package me.imshy.bankingInfo.general.http.request;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.general.util.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Response(Map<String, String> headers, String body, int code) {

  public List<String> getErrorDescriptions() {
    List<JsonNode> errors = JsonUtils.findValuesByKey("errors", body);
    return errors.stream().map(errorNode -> errorNode.get("description").textValue()).collect(Collectors.toList());
  }

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
