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

  private final HttpClientCreator httpClientCreator;

  private ApacheHttpClient(HttpClientCreator httpClientCreator) {
    this.httpClientCreator = httpClientCreator;
  }

  public ApacheHttpClient() {
    this.httpClientCreator = new HttpClientCreator();
  }

  @Override
  public Response fetch(JsonPostRequest jsonPostRequest) {
    try (CloseableHttpClient client = httpClientCreator.createClient()) {
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

    private final HttpClientCreator httpClientCreator = new HttpClientCreator();

    public Builder setProxy(URI uri) {
      httpClientCreator.setProxy(uri);
      return this;
    }

    public Builder trustAll() {
      httpClientCreator.trustAll();
      return this;
    }

    public ApacheHttpClient build() {
      return new ApacheHttpClient(httpClientCreator);
    }

  }

}
