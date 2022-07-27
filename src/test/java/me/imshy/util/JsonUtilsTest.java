package me.imshy.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
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
    void getValueFromJson_shouldSuccess() throws JsonProcessingException {
        String value = JsonUtils.getValueFromJson("guid", randomJson).replace("\"", "");
        assertThat(value).isEqualTo("4f88c4fc-71c2-4dbf-8b91-906c4c1c9522");
    }

    @Test
    void whenKeyNotFound_getValueFromJson_shouldReturnNull() throws JsonProcessingException {
        String value = JsonUtils.getValueFromJson("flow_id", randomJson);
        assertThat(value).isNull();
    }

    @Test
    void getNestedValueFromJson_shouldSuccess() throws JsonProcessingException {
        String value = JsonUtils.getNestedValueFromJson(new String[]{"name", "first"}, randomJson).replace("\"", "");
        assertThat(value).isEqualTo("Simon");
    }

    @Test
    void whenKeyNotFound_getNestedValueFromJson_shouldReturnNull() throws JsonProcessingException {
        String value = JsonUtils.getNestedValueFromJson(new String[]{"name", "third", "second"}, randomJson);
        assertThat(value).isNull();
    }

    @Test
    void parseFlatStringArray_shouldSuccess() throws JsonProcessingException {
        List<String> stringList = JsonUtils.parseFlatStringArray(arrayAsString);
        assertThat(stringList.size()).isEqualTo(2);
        assertThat(stringList.get(0)).isEqualTo("id1");
    }
}