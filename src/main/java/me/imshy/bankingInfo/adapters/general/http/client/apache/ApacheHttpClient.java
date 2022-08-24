package me.imshy.bankingInfo.adapters.general.http.client.apache;

import me.imshy.bankingInfo.adapters.general.exception.HttpCodeError;
import me.imshy.bankingInfo.adapters.general.http.client.apache.util.ApacheRequests;
import me.imshy.bankingInfo.domain.general.http.HttpClient;
import me.imshy.bankingInfo.domain.general.http.request.JsonPostRequest;
import me.imshy.bankingInfo.domain.general.http.request.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.IOException;

public class ApacheHttpClient implements HttpClient {
  @Override
  public Response fetch(JsonPostRequest jsonPostRequest) {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      CloseableHttpResponse response = client.execute(ApacheRequests.mapRequestToApache(jsonPostRequest));
      return handleResponse(response);
    } catch (IOException e) {
      throw new RuntimeException(e.getCause());
    }
  }

  private Response handleResponse(CloseableHttpResponse response) {
    try (response) {
      Response requestResponse = ApacheRequests.parseApacheResponse(response);
      if (requestResponse.code() >= 400) {
        throw new HttpCodeError(requestResponse.code());
      }
      return requestResponse;
    } catch (IOException e) {
      throw new RuntimeException(e.getCause());
    }
  }
}
