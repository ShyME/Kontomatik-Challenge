package me.imshy.scraper.infrastructure.apache;

import me.imshy.scraper.domain.http.HttpClient;
import me.imshy.scraper.domain.http.HttpCodeError;
import me.imshy.scraper.domain.http.JsonPostRequest;
import me.imshy.scraper.domain.http.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public class ApacheHttpClient implements HttpClient {

  private final Supplier<CloseableHttpClient> httpClientSupplier;

  public ApacheHttpClient(Supplier<CloseableHttpClient> httpClientSupplier) {
    this.httpClientSupplier = httpClientSupplier;
  }

  public ApacheHttpClient() {
    this.httpClientSupplier = HttpClients::createDefault;
  }

  @Override
  public Response fetch(JsonPostRequest jsonPostRequest) {
    try (CloseableHttpClient client = httpClientSupplier.get()) {
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

}
