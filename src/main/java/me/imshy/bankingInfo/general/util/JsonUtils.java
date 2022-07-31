package me.imshy.bankingInfo.general.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.List;
import java.util.Optional;

public class JsonUtils {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

  public static Optional<String> getValueFromJson(String key, String json) {
    try {
      JsonNode root = objectMapper.readTree(json);
      JsonNode foundValue = root.findValue(key);
      if (foundValue == null) {
        return Optional.empty();
      } else {
        return Optional.of(foundValue.toString());
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Json processing failed");
  }

  public static String getJson(Object requestBody) {
    try {
      return objectWriter.writeValueAsString(requestBody);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Json processing failed");
  }

  public static Optional<String> getNestedValueFromJson(String[] keys, String json) {
    try {
      JsonNode root = objectMapper.readTree(json);
      for (String key : keys) {
        if (root.hasNonNull(key)) {
          root = root.get(key);
        } else {
          return Optional.empty();
        }
      }
      return Optional.of(root.toString());
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
}
