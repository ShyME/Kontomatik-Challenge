package me.imshy.web.request.body;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class InitRequestBody {
    private final Map<String, Object> data = new HashMap<>();

    public InitRequestBody() {
        Map<Object, Object> emptyDictionary = new HashMap<>();
        data.put("account_ids", emptyDictionary);
        data.put("accounts", emptyDictionary);
    }
}
