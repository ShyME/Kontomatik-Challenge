package me.imshy.web.request.body;

import lombok.Getter;
import me.imshy.web.bankConnection.pko.SessionAttributes;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PasswordRequestBody {
    private final Map<String, Object> data = new HashMap<>();
    private final String state_id = "password";
    private final String action = "submit";

    private final String flow_id;
    private final String token;

    public PasswordRequestBody(String password, SessionAttributes sessionAttributes) {
        data.put("password", password);
        this.token = sessionAttributes.getToken();
        this.flow_id = sessionAttributes.getFlowId();
    }
}
