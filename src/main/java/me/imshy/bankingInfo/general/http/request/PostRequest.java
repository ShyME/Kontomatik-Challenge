package me.imshy.bankingInfo.general.http.request;

import me.imshy.bankingInfo.general.util.EntityUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class PostRequest {
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
    for (Header header : httpPost.getHeaders()) {
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

  public ClassicHttpRequest getHttpPost() {
    return httpPost;
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
