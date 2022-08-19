package me.imshy.bankingInfo.domain.pko;

import me.imshy.bankingInfo.CredentialsFileReader;
import me.imshy.bankingInfo.adapters.general.http.client.HttpClient;
import me.imshy.bankingInfo.adapters.general.http.client.apache.ApacheHttpClient;
import me.imshy.bankingInfo.domain.accountDetails.Account;
import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.exception.UnsuccessfulSignIn;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PkoAccountInfoProviderTest {
  private final HttpClient httpClient = new ApacheHttpClient();

  @Test
  void getAccountsInfo_shouldSuccess() {
    PkoAccountInfoProvider pkoAccountInfoProvider = new PkoAccountInfoProvider(
        httpClient,
        CredentialsFileReader.readCredentials()
    );

    List<Account> accountList = pkoAccountInfoProvider.getAccountsInfo();

    assertThat(accountList.size()).isEqualTo(1);
    assertThat(accountList.get(0).currency()).isEqualTo("PLN");
  }

  @Test
  void badLogin_getAccountsInfo_shouldThrowUnsuccessfulSignIn() {
    Credentials badLoginCredentials = new Credentials("abcdefghijk", "password");

    assertThatThrownBy(() -> {
      new PkoAccountInfoProvider(
          httpClient,
          badLoginCredentials
      );
    }).isInstanceOf(UnsuccessfulSignIn.class);
  }

  @Test
  void badPassword_getAccountsInfo_shouldThrowUnsuccessfulSignIn() {
    Credentials badPasswordCredentials = new Credentials(
        CredentialsFileReader.readCredentials().login(),
        "badPassword"
    );

    assertThatThrownBy(() -> {
      new PkoAccountInfoProvider(
          httpClient,
          badPasswordCredentials
      );
    }).isInstanceOf(UnsuccessfulSignIn.class);
  }
}