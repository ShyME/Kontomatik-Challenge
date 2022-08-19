package me.imshy.bankingInfo.adapters.general.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilsTest {

  private final String TEST_JSON = """
      {
          "_id": "62e1358a33a47da603c347d5",
          "index": 0,
          "guid": "4f88c4fc-71c2-4dbf-8b91-906c4c1c9522",
          "isActive": false,
          "age": 31,
          "first": true,
          "eyeColor": "green",
          "name": {
              "first": "Simon",
              "second": "Pakulski"
          },
          "gender": "male"
        }
      """;

  private final String ARRAY_AS_STRING = """
      [
          "id1",
          "id2"
      ]
      """;

  @Test
  void parseFlatStringArray_shouldSuccess() {
    List<String> stringList = JsonUtils.parseFlatStringArray(ARRAY_AS_STRING);
    assertThat(stringList.size()).isEqualTo(2);
    assertThat(stringList.get(0)).isEqualTo("id1");
  }

  @Test
  void findValuesByKey_shouldSuccess() {
    List<JsonNode> foundValues = JsonUtils.findValuesByKey("first", TEST_JSON);
    assertThat(foundValues.size()).isEqualTo(2);
  }
}