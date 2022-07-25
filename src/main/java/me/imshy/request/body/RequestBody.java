package me.imshy.request.body;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import me.imshy.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class RequestBody {
    private final Map<String, Object> data;

    protected RequestBody() {
        data = new HashMap<>(1);
    }

    protected void addData(String key, Object val) {
        data.put(key, val);
    }
}
