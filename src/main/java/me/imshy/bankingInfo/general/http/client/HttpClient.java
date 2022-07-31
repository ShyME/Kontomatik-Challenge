package me.imshy.bankingInfo.general.http.client;

import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.RequestResponse;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.Closeable;
import java.io.IOException;

public class HttpClient implements Closeable {

  private final CloseableHttpClient client;

  public HttpClient() {
    client = HttpClientBuilder.create()
        .build();
  }

  public RequestResponse sendRequest(PostRequest postRequest) {
    try {
      CloseableHttpResponse response = client.execute(postRequest.getHttpPost());
      RequestResponse requestResponse = new RequestResponse(response);
      return requestResponse;
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Sending request failed");
  }

  @Override
  public void close() throws IOException {
    client.close();
  }
}
