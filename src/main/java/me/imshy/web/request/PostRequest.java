package me.imshy.web.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import me.imshy.util.JsonUtils;
import me.imshy.web.bankConnection.pko.util.ResponseParserUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PostRequest {
    private static final Logger LOGGER = LogManager.getLogger(PostRequest.class);

    @Getter
    private final HttpPost httpPost;

    private PostRequest(PostRequestBuilder postRequestBuilder) {
        httpPost = postRequestBuilder.httpPost;
    }

    protected void setHeader(String headerName, String headerValue) {
        httpPost.setHeader(headerName, headerValue);
    }

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
        try {
            sb.append(EntityUtils.toString(httpPost.getEntity()));
        } catch (IOException | ParseException e) {
            LOGGER.error(e.getMessage());
        }
        sb.append("}");
        return sb.toString();
    }

    public static class PostRequestBuilder {
        private final HttpPost httpPost;

        public PostRequestBuilder(String requestUrl) {
            httpPost = new HttpPost(requestUrl);
            addJsonContentHeaders();
        }

        public PostRequestBuilder addHeader(String headerName, String headerValue) {
            httpPost.setHeader(headerName, headerValue);
            return this;
        }

        public PostRequestBuilder requestBody(StringEntity requestBodyJson) {
            httpPost.setEntity(requestBodyJson);
            return this;
        }

        public PostRequest build() {
            return new PostRequest(this);
        }

        private void addJsonContentHeaders() {
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
        }
    }

}
