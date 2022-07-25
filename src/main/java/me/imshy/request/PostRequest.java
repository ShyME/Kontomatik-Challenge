package me.imshy.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import me.imshy.request.body.RequestBody;
import me.imshy.util.JsonUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class PostRequest {

    @Getter
    private final HttpPost httpPost;

    public PostRequest(String requestUrl, RequestBody requestBody) throws JsonProcessingException {
        httpPost = new HttpPost(requestUrl);

        setDefaultHeaders();
        setRequestBody(requestBody);
    }

    private void setDefaultHeaders() {
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
    }

    private void setRequestBody(RequestBody requestBody) throws JsonProcessingException {
        httpPost.setEntity(new StringEntity(JsonUtils.getJson(requestBody)));
    }

    protected void setHeader(String headerName, String headerValue) {
        httpPost.setHeader(headerName, headerValue);
    }

}
