package me.imshy.scraper.infrastructure.apache;

import me.imshy.scraper.domain.http.HttpClient;
import me.imshy.scraper.domain.http.exception.HttpCodeError;
import me.imshy.scraper.domain.http.request.JsonPostRequest;
import me.imshy.scraper.domain.http.request.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

public class ApacheHttpClient implements HttpClient {

  private final CloseableHttpClientInitializer httpClientInitializer;

  private ApacheHttpClient(CloseableHttpClientInitializer httpClientInitializer) {
    this.httpClientInitializer = httpClientInitializer;
  }

  public ApacheHttpClient() {
    this.httpClientInitializer = new CloseableHttpClientInitializer();
  }

  @Override
  public Response fetch(JsonPostRequest jsonPostRequest) {
    try (CloseableHttpClient client = httpClientInitializer.createClient()) {
      CloseableHttpResponse response = client.execute(ApacheRequests.mapRequestToApache(jsonPostRequest));
      return handleResponse(response);
    } catch (IOException e) {
      throw new RuntimeException(e);
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
      throw new UncheckedIOException(e);
    }
  }

  public static class Builder {

    private final CloseableHttpClientInitializer clientInitializer = new CloseableHttpClientInitializer();

    public Builder setProxy(URI uri) {
      clientInitializer.setProxy(uri);
      return this;
    }

    public Builder trustAll() {
      clientInitializer.trustAll();
      return this;
    }

    public ApacheHttpClient build() {
      return new ApacheHttpClient(clientInitializer);
    }

  }

}
