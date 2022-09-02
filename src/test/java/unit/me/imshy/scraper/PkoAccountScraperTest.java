package unit.me.imshy.scraper;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import me.imshy.scraper.domain.Account;
import me.imshy.scraper.domain.PkoAccountScraper;
import me.imshy.scraper.domain.exception.AccessBlocked;
import me.imshy.scraper.domain.exception.InvalidCredentials;
import me.imshy.scraper.infrastructure.apache.ApacheHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PkoAccountScraperTest {

  @RegisterExtension
  private static final WireMockExtension wireMock = WireMockExtension.newInstance()
      .options(wireMockConfig().dynamicHttpsPort())
      .proxyMode(true)
      .build();

  @Test
  void importAccounts() {
    PkoAccountScraper pkoAccountScraper = createWireMockTestPkoScraper();
    PkoWireMock.mockSuccessfulImport();
    List<Account> accounts = pkoAccountScraper.importAccounts(PkoWireMock.CREDENTIALS);
    assertThat(accounts).singleElement().isEqualTo(PkoWireMock.ACCOUNT);
  }

  @Test
  void importAccountsThrowsInvalidCredentials() {
    PkoAccountScraper pkoAccountScraper = createWireMockTestPkoScraper();
    PkoWireMock.mockInvalidCredentials();
    assertThatThrownBy(() -> pkoAccountScraper.importAccounts(PkoWireMock.CREDENTIALS))
        .isInstanceOf(InvalidCredentials.class);
  }

  @Test
  void importAccountsThrowsAccessBlocked() {
    PkoAccountScraper pkoAccountScraper = createWireMockTestPkoScraper();
    PkoWireMock.mockAccessBlocked();
    assertThatThrownBy(() -> pkoAccountScraper.importAccounts(PkoWireMock.CREDENTIALS))
        .isInstanceOf(AccessBlocked.class);
  }

  @Test
  void importAccountsThrowsAccessBlockedOnCaptcha() {
    PkoAccountScraper pkoAccountScraper = createWireMockTestPkoScraper();
    PkoWireMock.mockLoginCaptcha();
    assertThatThrownBy(() -> pkoAccountScraper.importAccounts(PkoWireMock.CREDENTIALS))
        .isInstanceOf(AccessBlocked.class);
  }

  private static PkoAccountScraper createWireMockTestPkoScraper() {
    WireMockRuntimeInfo wireMockRuntimeInfo = wireMock.getRuntimeInfo();
    return new PkoAccountScraper(
        new ApacheHttpClient(
            new TrustAllProxiedHttpClientSupplier(URI.create(wireMockRuntimeInfo.getHttpsBaseUrl()))
        )
    );
  }

}