package me.imshy.bankingInfo.adapters.general.http.client.apache;

import me.imshy.bankingInfo.adapters.general.exception.RequestError;
import me.imshy.bankingInfo.adapters.general.http.client.apache.util.ApacheRequests;
import me.imshy.bankingInfo.adapters.general.http.request.PostRequest;
import me.imshy.bankingInfo.adapters.general.http.request.Response;
import me.imshy.bankingInfo.adapters.general.http.client.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class ApacheHttpClient implements HttpClient {

  @Override
  public Response fetchRequest(PostRequest postRequest) {
    try(CloseableHttpClient client = HttpClientBuilder.create().build()) {
      CloseableHttpResponse response = client.execute(ApacheRequests.mapRequestToApache(postRequest));

      return handleResponse(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Sending request failed");
  }

  private Response handleResponse(CloseableHttpResponse response) {
    try(response) {
      Response requestResponse = ApacheRequests.parseApacheResponse(response);

      List<String> errors = requestResponse.getErrorDescriptions();
      if(requestResponse.code() >= 400 || errors.size() > 0) {
        throw new RequestError(requestResponse.code(), errors);
      }

      return requestResponse;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
