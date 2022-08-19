package me.imshy.bankingInfo.domain.pko.http;


import me.imshy.bankingInfo.CredentialsFileReader;
import me.imshy.bankingInfo.adapters.general.http.client.apache.ApacheHttpClient;
import me.imshy.bankingInfo.domain.accountDetails.Account;
import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PkoConnectionTest {
  @Test
  void getAccountBalances_shouldSuccess() {
    PkoConnection pkoConnection = createValidPkoConnection();

    List<Account> accountList = pkoConnection.getAccountBalances();
    assertThat(accountList.size()).isGreaterThan(0);
  }

  @Test
  void logout_shouldSuccess() {
    PkoConnection pkoConnection = createValidPkoConnection();

    pkoConnection.logout();
  }

  private PkoConnection createValidPkoConnection() {
    Credentials credentials = CredentialsFileReader.readCredentials();
    PkoSignIn pkoSignIn = new PkoSignIn(new ApacheHttpClient());
    return pkoSignIn.login(credentials);
  }
}