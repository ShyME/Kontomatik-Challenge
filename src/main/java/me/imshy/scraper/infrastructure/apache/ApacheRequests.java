package me.imshy.scraper.infrastructure.apache;

import me.imshy.scraper.domain.http.JsonPostRequest;
import me.imshy.scraper.domain.http.Response;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;

class ApacheRequests {

  static HttpPost mapRequestToApache(JsonPostRequest jsonPostRequest) {
    var httpPost = new HttpPost(jsonPostRequest.url());
    httpPost.setEntity(new StringEntity(jsonPostRequest.body()));
    jsonPostRequest.headers().forEach(httpPost::addHeader);
    return httpPost;
  }

  static Response parseApacheResponse(CloseableHttpResponse httpResponse) {
    var headerMap = new HashMap<String, String>();
    Header[] headers = httpResponse.getHeaders();
    for (Header header : headers) {
      headerMap.put(header.getName(), header.getValue());
    }
    String body = ApacheRequests.toString(httpResponse.getEntity());
    return new Response(
      headerMap,
      body,
      httpResponse.getCode()
    );
  }

  private static String toString(HttpEntity httpEntity) {
    try {
      return EntityUtils.toString(httpEntity);
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

}
