package me.imshy.web.request.body;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class LogoutRequestBody {
    private final Map<String, Object> data = new HashMap<>();

    public LogoutRequestBody() {
        data.put("reason", "CLIENT_LOGOUT");
    }
}
