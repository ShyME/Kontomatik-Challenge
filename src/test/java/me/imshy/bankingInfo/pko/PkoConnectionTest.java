package me.imshy.bankingInfo.pko;


import me.imshy.bankingInfo.CredentialsFileReader;
import me.imshy.bankingInfo.general.accountDetails.Account;
import me.imshy.bankingInfo.general.accountDetails.Credentials;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PkoConnectionTest {
  private final String credentialsFilePath = "credentials.txt";

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
    Credentials credentials = CredentialsFileReader.readCredentials(credentialsFilePath);
    PkoSignIn pkoSignIn = new PkoSignIn();
    return pkoSignIn.login(credentials);
  }
}