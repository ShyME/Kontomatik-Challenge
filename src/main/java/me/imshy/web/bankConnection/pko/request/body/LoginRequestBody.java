package me.imshy.web.bankConnection.pko.request.body;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class LoginRequestBody {
    private final Map<String, Object> data = new HashMap<>();
    private final String state_id = "login";
    private final String action = "submit";

    public LoginRequestBody(String login) {
        data.put("login", login);
    }
}
