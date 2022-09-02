package unit.me.imshy.scraper;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
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
import java.util.function.Supplier;

public class TrustAllProxiedHttpClientSupplier implements Supplier<CloseableHttpClient> {

  private final HttpHost proxyHost;

  public TrustAllProxiedHttpClientSupplier(URI proxy) {
    proxyHost = new HttpHost(proxy.getScheme(), proxy.getHost(), proxy.getPort());
  }

  @Override
  public CloseableHttpClient get() {
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    httpClientBuilder.setProxy(proxyHost);
    httpClientBuilder.setConnectionManager(createAllTrustingConnectionManager());
    return httpClientBuilder.build();
  }

  PoolingHttpClientConnectionManager createAllTrustingConnectionManager() {
    try {
      SSLContext sslcontext = SSLContexts.custom()
        .loadTrustMaterial(null, new TrustAllStrategy())
        .build();
      SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
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
