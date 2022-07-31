package me.imshy.bankingInfo.general.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilsTest {

  private final String randomJson = """
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

  private final String arrayAsString = """
      [
          "id1",
          "id2"
      ]
      """;

  @Test
  void getValueFromJson_shouldSuccess() {
    Optional<String> value = JsonUtils.getValueFromJson("guid", randomJson);
    assertThat(value.get().replace("\"", "")).isEqualTo("4f88c4fc-71c2-4dbf-8b91-906c4c1c9522");
  }

  @Test
  void whenKeyNotFound_getValueFromJson_shouldReturnEmpty() {
    Optional<String> value = JsonUtils.getValueFromJson("flow_id", randomJson);
    assertThat(value).isEqualTo(Optional.empty());
  }

  @Test
  void getNestedValueFromJson_shouldSuccess() {
    String value = JsonUtils.getNestedValueFromJson(new String[]{"name", "first"}, randomJson).get().replace("\"", "");
    assertThat(value).isEqualTo("Simon");
  }

  @Test
  void whenKeyNotFound_getNestedValueFromJson_shouldReturnEmpty() {
    Optional<String> value = JsonUtils.getNestedValueFromJson(new String[]{"name", "third", "second"}, randomJson);
    assertThat(value).isEqualTo(Optional.empty());
  }

  @Test
  void parseFlatStringArray_shouldSuccess() {
    List<String> stringList = JsonUtils.parseFlatStringArray(arrayAsString);
    assertThat(stringList.size()).isEqualTo(2);
    assertThat(stringList.get(0)).isEqualTo("id1");
  }
}