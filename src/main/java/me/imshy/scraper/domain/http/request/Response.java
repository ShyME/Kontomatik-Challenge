package me.imshy.scraper.domain.http.request;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.scraper.infrastructure.json.JsonUtils;

import java.util.Map;

public record Response(Map<String, String> headers, String body, int code) {

  public JsonNode toJson() {
    return JsonUtils.getJsonAsNode(body);
  }

}
