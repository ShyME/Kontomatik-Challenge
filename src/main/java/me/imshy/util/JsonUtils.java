package me.imshy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

    public static String getValueFromJson(String key, String json) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(json);
        return root.get(key).toString();
    }

    public static String getJson(Object requestBody) throws JsonProcessingException {
        return objectWriter.writeValueAsString(requestBody);
    }

    public static String getNestedValueFromJson(String[] keyPath, String json) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(json);
        for(String key : keyPath) {
            root = root.get(key);
        }
        return root.toString();
    }

    public static List<String> parseFlatStringArray(String stringArrayJson) {
        List<String> strings = new ArrayList<>(1);
        StringBuilder stringArray = new StringBuilder(stringArrayJson);

        int quoteIndex = 0;
        while((quoteIndex = stringArray.indexOf("\"")) != -1) {
            int secondQuoteIndex = stringArray.indexOf("\"", quoteIndex+1);
            String elem = stringArray.substring(quoteIndex+1, secondQuoteIndex);
            stringArray.delete(quoteIndex, secondQuoteIndex+1);
            strings.add(elem);
        }

        System.out.println(strings);
        return strings;
    }
}
