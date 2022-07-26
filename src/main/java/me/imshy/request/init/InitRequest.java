package me.imshy.request.init;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.request.SessionPostRequest;

public class InitRequest extends SessionPostRequest {
    public InitRequest(String requestUrl, String sessionId) throws JsonProcessingException {
        super(requestUrl, new InitRequestBody(), sessionId);
    }
}
