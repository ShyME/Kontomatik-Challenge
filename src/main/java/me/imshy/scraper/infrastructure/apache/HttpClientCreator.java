package me.imshy.scraper.infrastructure.apache;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

class HttpClientCreator {

  private HttpHost proxyHost;
  private boolean trustAll = false;

  void setProxy(URI uri) {
    proxyHost = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort());
  }

  void trustAll() {
    this.trustAll = true;
  }

  CloseableHttpClient createClient() {
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    if (proxyHost != null) {
      HttpRoutePlanner proxyRoutePlanner = new DefaultProxyRoutePlanner(proxyHost);
      httpClientBuilder.setRoutePlanner(proxyRoutePlanner);
    }
    if (trustAll) {
      httpClientBuilder.setConnectionManager(createAllTrustingConnectionManager());
    }
    return httpClientBuilder.build();
  }

  PoolingHttpClientConnectionManager createAllTrustingConnectionManager() {
    try {
      final SSLContext sslcontext = SSLContexts.custom()
          .loadTrustMaterial(null, new TrustAllStrategy())
          .build();
      final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
          .setSslContext(sslcontext)
          .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
          .build();
      return PoolingHttpClientConnectionManagerBuilder.create()
          .setSSLSocketFactory(sslSocketFactory)
          .build();
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

}