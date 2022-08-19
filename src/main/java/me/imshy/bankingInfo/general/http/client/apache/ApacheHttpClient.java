package me.imshy.bankingInfo.general.http.client.apache;

import me.imshy.bankingInfo.general.exception.RequestError;
import me.imshy.bankingInfo.general.http.client.HttpClient;
import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class ApacheHttpClient implements HttpClient {

  private final CloseableHttpClient client;

  public ApacheHttpClient() {
    client = HttpClientBuilder.create()
        .build();
  }

  @Override
  public Response fetchRequest(PostRequest postRequest) {
    try {
      CloseableHttpResponse response = client.execute(ApacheRequests.mapRequestToApache(postRequest));
      Response requestResponse = ApacheRequests.parseApacheResponse(response);

      List<String> errors = requestResponse.getErrorDescriptions();
      if(requestResponse.code() >= 400 || errors.size() > 0) {
        throw new RequestError(requestResponse.code(), errors);
      }

      return requestResponse;
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Sending request failed");
  }
}
