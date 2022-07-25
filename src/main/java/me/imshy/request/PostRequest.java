package me.imshy.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import me.imshy.request.body.RequestBody;
import me.imshy.util.JsonUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.EntityUtils;
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

    @SneakyThrows
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PostRequest={url=");
        sb.append(httpPost.getRequestUri());
        sb.append(",headers=[");
        for(Header header : httpPost.getHeaders()) {
            sb.append(header.getName());
            sb.append(": ");
            sb.append(header.getValue());
            sb.append(",");
        }
        sb.append("],body=");
        sb.append(EntityUtils.toString(httpPost.getEntity()));
        sb.append("}");
        return sb.toString();
    }

}
