package me.imshy.bankingInfo.general.http.client.apache;

import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.Response;
import me.imshy.bankingInfo.general.http.client.apache.util.EntityUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.HashMap;
import java.util.Map;

public class ApacheRequests {

  public static HttpPost mapRequestToApache(PostRequest postRequest) {
    HttpPost httpPost = new HttpPost(postRequest.url());
    httpPost.setEntity(new StringEntity(postRequest.body()));
    for(Map.Entry<String, String> headerEntry : postRequest.headers().entrySet()) {
      httpPost.addHeader(headerEntry.getKey(), headerEntry.getValue());
    }
    return httpPost;
  }

  public static Response parseApacheResponse(CloseableHttpResponse httpResponse) {
    Map<String, String> headerMap = new HashMap<>();

    Header[] headers = httpResponse.getHeaders();
    for(Header header : headers) {
      headerMap.put(header.getName(), header.getValue());
    }

    return new Response.ResponseBuilder()
        .setHeaders(headerMap)
        .setBody(EntityUtils.toString(httpResponse.getEntity()))
        .setCode(httpResponse.getCode())
        .build();
  }
}
