package me.imshy.request.logout;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.request.SessionPostRequest;

public class LogoutRequest extends SessionPostRequest {
    public LogoutRequest(String requestUrl, String sessionId) throws JsonProcessingException {
        super(requestUrl, new LogoutRequestBody(), sessionId);
    }
}
