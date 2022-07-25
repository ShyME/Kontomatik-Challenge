package me.imshy.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.request.body.RequestBody;

public class SessionPostRequest extends PostRequest {

    public SessionPostRequest(String requestUrl, RequestBody requestBody, String sessionId) throws JsonProcessingException {
        super(requestUrl, requestBody);
        setHeader("x-session-id", sessionId);
    }
}
