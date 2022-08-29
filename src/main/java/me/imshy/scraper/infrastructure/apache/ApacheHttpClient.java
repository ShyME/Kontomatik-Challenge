package me.imshy.scraper.infrastructure.apache;

import me.imshy.scraper.domain.http.HttpClient;
import me.imshy.scraper.domain.http.exception.HttpCodeError;
import me.imshy.scraper.domain.http.request.JsonPostRequest;
import me.imshy.scraper.domain.http.request.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.HttpHost;

import java.io.IOException;
import java.io.UncheckedIOException;

public class ApacheHttpClient implements HttpClient {

  private HttpHost proxy;

  public ApacheHttpClient() {
  }

  public ApacheHttpClient(String proxyUrl) {
    this.proxy = new HttpHost(proxyUrl);
  }

  @Override
  public Response fetch(JsonPostRequest jsonPostRequest) {
    try (CloseableHttpClient client = createClient()) {
      CloseableHttpResponse response = client.execute(ApacheRequests.mapRequestToApache(jsonPostRequest));
      return handleResponse(response);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private CloseableHttpClient createClient() {
    if (proxy == null) {
      return HttpClients.createDefault();
    } else {
      return createProxiedClient();
    }
  }

  private CloseableHttpClient createProxiedClient() {
    HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
    return HttpClients.custom()
        .setRoutePlanner(routePlanner)
        .build();
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
