package integration.me.imshy.scraper.domain;

import me.imshy.scraper.Application;
import me.imshy.scraper.domain.Credentials;
import me.imshy.scraper.domain.exception.InvalidCredentials;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  void scrapesAndPrintsAccounts() {
    StdIOStub.input(CredentialsFileReader.readCredentials());
    StdIOStub.around(Application::main);
    Assertions.assertThat(StdIOStub.getOutput())
      .containsPattern("Account\\[accountNumber=\\d{26},\\s?currency=\\w+,\\sbalance=\\d+\\.\\d{2}]");
  }

  @Test
  void scrapesAndPrintsAccountsThrowsInvalidCredentials() {
    StdIOStub.input(new Credentials("login", "badPassword"));
    Assertions.assertThatThrownBy(Application::main)
      .isInstanceOf(InvalidCredentials.class);
  }

}