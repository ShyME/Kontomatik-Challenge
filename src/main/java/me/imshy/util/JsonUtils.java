package me.imshy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.List;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

    public static String getValueFromJson(String key, String json) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(json);
        return root.findValue(key).toString();
    }

    public static String getJson(Object requestBody) throws JsonProcessingException {
        return objectWriter.writeValueAsString(requestBody);
    }

    public static String getNestedValueFromJson(String[] keys, String json) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(json);
        for(String key : keys) {
            if(root.hasNonNull(key)) {
                root = root.get(key);
            } else {
                return null;
            }
        }
        return root.toString();
    }

    public static List<String> parseFlatStringArray(String stringArrayJson) throws JsonProcessingException {
        return objectMapper.readValue(stringArrayJson, new TypeReference<>() {});
    }
}
