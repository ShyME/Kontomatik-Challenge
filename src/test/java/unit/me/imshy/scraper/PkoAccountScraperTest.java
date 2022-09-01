package unit.me.imshy.scraper;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import me.imshy.scraper.domain.Account;
import me.imshy.scraper.domain.PkoAccountScraper;
import me.imshy.scraper.domain.exception.AccessBlocked;
import me.imshy.scraper.domain.exception.InvalidCredentials;
import me.imshy.scraper.infrastructure.apache.ApacheHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest(proxyMode = true)
class PkoAccountScraperTest {
  private static final PkoWireMock pkoWireMock = new PkoWireMock();

  @AfterEach
  void assertNoUnmatchedRequests() {
    assertThat(WireMock.findUnmatchedRequests()).isEmpty();
  }

  @Test
  void importAccounts(WireMockRuntimeInfo wireMockRuntimeInfo) {
    PkoAccountScraper pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpBaseUrl());
    pkoWireMock.mockSuccessfulImport();

    List<Account> accounts = pkoAccountScraper.importAccounts(pkoWireMock.CREDENTIALS);

    Account expected = new Account("01234567890123456789012345", "PLN", new BigDecimal("1000.00"));
    assertThat(accounts.get(0)).isEqualTo(expected);
  }

  @Test
  void importAccountsAccessBlockedCaptcha(WireMockRuntimeInfo wireMockRuntimeInfo) {
    PkoAccountScraper pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpBaseUrl());
    pkoWireMock.mockLoginCaptcha();
    assertThatThrownBy(() -> pkoAccountScraper.importAccounts(pkoWireMock.CREDENTIALS))
        .isInstanceOf(AccessBlocked.class);
  }

  @Test
  void importAccountsInvalidCredentials(WireMockRuntimeInfo wireMockRuntimeInfo) {
    PkoAccountScraper pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpBaseUrl());
    pkoWireMock.mockInvalidCredentials();
    assertThatThrownBy(() -> pkoAccountScraper.importAccounts(pkoWireMock.CREDENTIALS))
        .isInstanceOf(InvalidCredentials.class);
  }

  @Test
  void importAccountsAccountBlocked(WireMockRuntimeInfo wireMockRuntimeInfo) {
    PkoAccountScraper pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpBaseUrl());
    pkoWireMock.mockAccessBlocked();
    assertThatThrownBy(() -> pkoAccountScraper.importAccounts(pkoWireMock.CREDENTIALS))
        .isInstanceOf(AccessBlocked.class);
  }

  private static PkoAccountScraper createTestScraperWithProxy(String proxyUrl) {
    return new PkoAccountScraper(
        new ApacheHttpClient(
            new TrustAllProxiedHttpClientSupplier(URI.create(proxyUrl))
        )
    );
  }

}