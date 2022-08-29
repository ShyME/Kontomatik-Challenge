package me.imshy.scraper.infrastructure.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.List;

public class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

  public static String getJsonAsString(Object object) {
    try {
      return objectWriter.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getCause());
    }
  }

  public static JsonNode getJsonAsNode(String json) {
    try {
      return objectMapper.readTree(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getCause());
    }
  }

  public static List<JsonNode> findValuesByKey(String key, String json) {
    try {
      JsonNode root = objectMapper.readTree(json);
      List<JsonNode> foundValues = root.findValues(key);
      return foundValues.stream()
          .filter(jsonNode -> !jsonNode.isNull())
          .toList();
    } catch (JsonProcessingException | NullPointerException e) {
      throw new RuntimeException(e.getCause());
    }
  }

}
