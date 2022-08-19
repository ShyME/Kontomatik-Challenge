package me.imshy.bankingInfo.general.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonUtilsTest {

  private final String TEST_JSON = """
      {
          "_id": "62e1358a33a47da603c347d5",
          "index": 0,
          "guid": "4f88c4fc-71c2-4dbf-8b91-906c4c1c9522",
          "isActive": false,
          "age": 31,
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
  void getValueFromJson_shouldSuccess() {
    String value = JsonUtils.getValueFromJson("guid", TEST_JSON);
    assertThat(value.replace("\"", "")).isEqualTo("4f88c4fc-71c2-4dbf-8b91-906c4c1c9522");
  }

  @Test
  void whenKeyNotFound_getValueFromJson_shouldThrowException() {
    assertThatThrownBy(() -> {
      JsonUtils.getValueFromJson("flow_id", TEST_JSON);
    }).isInstanceOf(RuntimeException.class);
  }

  @Test
  void nodeTraversal_shouldSuccess() {
    String value = JsonUtils.getJsonAsNode(TEST_JSON).get("name").get("first").textValue();
    assertThat(value).isEqualTo("Simon");
  }

  @Test
  void whenKeyNotFound_nodeTraversal_shouldThrowNullPointer() {
    assertThatThrownBy(() -> {
      JsonUtils.getJsonAsNode(TEST_JSON).get("name").get("third").get("second").textValue();
    }).isInstanceOf(NullPointerException.class);
  }

  @Test
  void parseFlatStringArray_shouldSuccess() {
    List<String> stringList = JsonUtils.parseFlatStringArray(ARRAY_AS_STRING);
    assertThat(stringList.size()).isEqualTo(2);
    assertThat(stringList.get(0)).isEqualTo("id1");
  }
}