package me.imshy.request.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.request.PostRequest;

public class LoginRequest extends PostRequest {
    public LoginRequest(String requestUrl, String login) throws JsonProcessingException {
        super(requestUrl, new LoginRequestBody(login));
    }
}
