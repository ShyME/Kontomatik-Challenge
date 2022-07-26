package me.imshy.request.password;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.bankConnection.pko.SessionAttributes;
import me.imshy.request.SessionPostRequest;

public class PasswordRequest extends SessionPostRequest {
    public PasswordRequest(String requestUrl, String password, SessionAttributes sessionAttributes) throws JsonProcessingException {
        super(requestUrl,
                new PasswordRequestBody(
                        password,
                        sessionAttributes.getToken(),
                        sessionAttributes.getFlowId()
                ),
                sessionAttributes.getSessionId()
        );
    }
}
