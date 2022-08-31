package me.imshy.scraper.domain;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import me.imshy.scraper.domain.http.exception.signIn.AccessBlocked;
import me.imshy.scraper.domain.http.exception.signIn.InvalidCredentials;
import me.imshy.scraper.infrastructure.apache.ApacheHttpClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@WireMockTest(proxyMode = true, httpsEnabled = true)
class PkoAccountScraperTest {

  private static final Credentials CREDENTIALS = new Credentials("LOGIN", "PASSWORD");
  private static final PkoWireMock pkoWireMock = new PkoWireMock(CREDENTIALS);

  @Test
  void importAccounts(WireMockRuntimeInfo wireMockRuntimeInfo) {
    var pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpsBaseUrl());
    pkoWireMock.mockValidLogin();
    pkoWireMock.mockValidPassword();
    pkoWireMock.mockInit();

    List<Account> accounts = pkoAccountScraper.importAccounts(CREDENTIALS);

    Assertions.assertThat(accounts).isNotEmpty();
    Assertions.assertThat(WireMock.findUnmatchedRequests()).isEmpty();
  }

  @Test
  void importAccountsAccessBlockedCaptcha(WireMockRuntimeInfo wireMockRuntimeInfo) {
    var pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpsBaseUrl());
    pkoWireMock.mockLoginCaptcha();

    Assertions.assertThatThrownBy(() -> pkoAccountScraper.importAccounts(CREDENTIALS))
        .isInstanceOf(AccessBlocked.class);
    Assertions.assertThat(WireMock.findUnmatchedRequests()).isEmpty();
  }

  @Test
  void importAccountsInvalidCredentials(WireMockRuntimeInfo wireMockRuntimeInfo) {
    var pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpsBaseUrl());
    pkoWireMock.mockValidLogin();
    pkoWireMock.mockInvalidPassword();

    Assertions.assertThatThrownBy(() -> pkoAccountScraper.importAccounts(CREDENTIALS))
        .isInstanceOf(InvalidCredentials.class);
    Assertions.assertThat(WireMock.findUnmatchedRequests()).isEmpty();
  }

  @Test
  void importAccountsAccountBlocked(WireMockRuntimeInfo wireMockRuntimeInfo) {
    var pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpsBaseUrl());
    pkoWireMock.mockValidLogin();
    pkoWireMock.mockAccountBlocked();

    Assertions.assertThatThrownBy(() -> pkoAccountScraper.importAccounts(CREDENTIALS))
        .isInstanceOf(AccessBlocked.class);
    Assertions.assertThat(WireMock.findUnmatchedRequests()).isEmpty();
  }

  private static PkoAccountScraper createTestScraperWithProxy(String baseUrl) {
    return new PkoAccountScraper(
        new ApacheHttpClient.Builder()
            .setProxy(getUri(baseUrl))
            .trustAll()
            .build()
    );
  }

  private static URI getUri(String baseUrl) {
    try {
      return new URI(baseUrl);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}