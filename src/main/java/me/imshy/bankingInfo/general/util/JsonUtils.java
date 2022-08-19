package me.imshy.bankingInfo.general.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.List;
import java.util.stream.Collectors;

public class JsonUtils {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

  public static String getValueFromJson(String key, String json) {
    try {
      JsonNode root = objectMapper.readTree(json);
      JsonNode foundValue = root.findValue(key);
      return foundValue.toString();
    } catch (JsonProcessingException | NullPointerException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Json processing failed");
  }

  public static String getJsonAsString(Object object) {
    try {
      return objectWriter.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Json processing failed");
  }

  public static JsonNode getJsonAsNode(String json) {
    try {
      return objectMapper.readTree(json);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Json processing failed");
  }

  public static List<String> parseFlatStringArray(String stringArrayJson) {
    try {
      return objectMapper.readValue(stringArrayJson, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Json processing failed");
  }

  public static List<JsonNode> findValuesByKey(String key, String json) {
    try {
      JsonNode root = objectMapper.readTree(json);
      List<JsonNode> foundValues = root.findValues(key);

      return foundValues.stream()
          .filter(jsonNode -> !jsonNode.isEmpty())
          .collect(Collectors.toList());
    } catch (JsonProcessingException | NullPointerException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Json processing failed");
  }
}
